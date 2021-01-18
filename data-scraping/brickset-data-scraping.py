import requests
from bs4 import BeautifulSoup
import re
import math
import logging
import logging.config
import yaml
import json
import os.path


def config():
    global config_file
    config_file_d = open("./config/brickset-scrapping.yaml")
    config_file = yaml.load(config_file_d, Loader=yaml.FullLoader)
    open(config_file['logging']['file'], "w").close()
    logging.basicConfig(filename=config_file['logging']['file'], level=logging.DEBUG,
                        format=config_file['logging']['format'])

    if not os.path.isfile(config_file['data']['endpoints_file']):
        open(config_file['data']['endpoints_file'], "w")
        logging.debug("File created: {0}".format(config_file['data']['endpoints_file']))

    if not os.path.isfile(config_file['data']['sets_by_theme_file']):
        sets_file = open(config_file['data']['sets_by_theme_file'], "w")
        sets_file.write("[]")
        sets_file.close()
        logging.debug("File created: {0}".format(config_file['data']['sets_by_theme_file']))

    if not os.path.isfile(config_file['data']['delta_file']):
        sets_file = open(config_file['data']['delta_file'], "w")
        sets_file.write("[]")
        sets_file.close()
        logging.debug("File created: {0}".format(config_file['data']['delta_file']))

    logging.debug("Script configured.")


def get_sets_page(url):
    response = requests.get(url)
    if response.status_code == 200:
        return response.text
    return None


def get_dict_of_endpoints(sets_page_html):
    soup = BeautifulSoup(sets_page_html, 'html.parser')
    list_of_brickset_endpoints = soup.find(class_="addtags").find_all("option")
    list_of_year_endpoints = []
    list_of_theme_endpoints = []
    for endpoint in list_of_brickset_endpoints:
        if endpoint.has_attr("value"):
            endpoint_value = endpoint["value"]
            if re.match("/sets/theme.*", endpoint_value):
                list_of_theme_endpoints.append(endpoint_value)
            else:
                list_of_year_endpoints.append(endpoint_value)

    endpoints = {"theme": list_of_theme_endpoints, "year": list_of_year_endpoints}
    logging.debug("Retrieve endpoints = {0}".format(endpoints))

    save_to_file(config_file['data']['endpoints_file'], endpoints)

    return endpoints


def save_to_file(file_name, data):
    logging.debug("Save data to file: {0}".format(file_name))
    with open(file_name, 'w') as outfile:
        json.dump(data, outfile, indent=4)


def get_list_of_single_theme_list(url):
    soup = get_soup_of_url(url)
    number_of_pages = get_number_of_pages(soup)
    single_them_list = []
    if number_of_pages > 1:
        for i in range(1, number_of_pages + 1):
            page_url = "{0}/page-{1}".format(url, i)
            logging.debug("Get data from: {0}".format(page_url))
            page_soup = get_soup_of_url(page_url)
            single_them_list += get_list_of_sets_data(page_soup)
    else:
        logging.debug("Get data from single page.")
        single_them_list += get_list_of_sets_data(soup)
    return single_them_list


def get_soup_of_url(url):
    response = requests.get(url)
    set_list_page_html = response.text
    soup = BeautifulSoup(set_list_page_html, 'html.parser')
    return soup


def get_list_of_sets_data(soup):
    sets_on_page = soup.find_all("article", class_="set")
    list_of_sets = []
    for article in sets_on_page:
        data = get_single_set_data(article)
        if data is not None:
            list_of_sets.append(data)
    logging.debug("Get list of sets.")
    return list_of_sets


def get_single_set_data(article):
    pieces = article.find("div", class_="col").find("a", class_="plain")
    if pieces is not None:
        year = article.find("a", class_="year").getText()
        tags = article.find("div", class_="tags").find_all("a")
        model_id = tags[0].getText()
        theme = tags[1].getText()
        img_link = article.find("img")["src"]
        pieces_url = pieces["href"]
        pieces_num = pieces.getText()
        data = {"model_id": model_id, "theme": theme, "year": year, "img_link": img_link,
                "pieces_num": pieces_num, "pieces_url": pieces_url}
        logging.debug("Retrieved data of model: {0}".format(data))
        return data
    return None


def get_number_of_pages(soup):
    number_of_sets = soup.find("div", class_="resultsfilter").find("div", class_="results").getText()
    number_of_sets_match = re.match(r'[0-9]* to (.*) of (.*) match[es]?', number_of_sets)
    number_of_pages = math.ceil(int(number_of_sets_match.group(2)) / int(number_of_sets_match.group(1)))
    return number_of_pages


def get_list_of_all_themes(api_url, endpoints_list):
    list_of_all_themes = []
    for url in endpoints_list:
        new_theme = {}
        logging.debug('Get data from: {0}{1}'.format(api_url, url))
        theme_list = get_list_of_single_theme_list('{0}{1}'.format(api_url, url))
        if len(theme_list) > 0:
            new_theme["name"] = theme_list[0]["theme"]
            new_theme["list"] = theme_list
            logging.debug("Save Theme: {0}".format(new_theme["name"]))
            list_of_all_themes.append(new_theme)
    return list_of_all_themes


def calculate_delta(file_name, sets_by_theme):
    delta = []
    logging.debug("Calculating delta.")
    with open(file_name) as json_file:
        data = json.load(json_file)
        if data:
            for theme in sets_by_theme:
                item = list(filter(lambda i: i["name"] == theme["name"], data))
                if not item:
                    delta.append(theme)
                else:
                    theme_delta = {"name": theme["name"], "list": []}
                    for model in theme["list"]:
                        found_model = list(filter(lambda i: i["model_id"] == model["model_id"], item[0]['list']))
                        if not found_model:
                            theme_delta["list"].append(model)
                    if theme_delta["list"]:
                        delta.append(theme_delta)
        else:
            delta = sets_by_theme
    logging.debug("Delta calculated")
    save_to_file(config_file['data']['delta_file'], delta)
    return delta


def upsert_sets_by_theme(file_name, delta):
    logging.debug("Read data from file: {0}".format(file_name))
    f = open(file_name, "r")
    data_from_file = json.load(f)
    f.close()
    for theme in delta:
        item = list(filter(lambda i: i["name"] == theme["name"], data_from_file))
        if not item:
            data_from_file.append(theme)
        else:
            for model in theme["list"]:
                found_model = list(filter(lambda i: i["model_id"] == model["model_id"], item[0]['list']))
                if not found_model:
                    item[0]['list'].append(model)
    logging.debug("Upserted delta to data.")
    return data_from_file


def main():
    config()
    logging.debug("Script started!")
    api_url = "https://brickset.com"
    sets_page_html = get_sets_page(api_url + "/sets")
    if sets_page_html is not None:
        logging.debug("Sets page exists.")
        endpoints_dict = get_dict_of_endpoints(sets_page_html)
        sets_by_theme = get_list_of_all_themes(api_url, endpoints_dict["theme"])
        delta = calculate_delta(config_file['data']['sets_by_theme_file'], sets_by_theme)
        upserted_data = upsert_sets_by_theme(config_file['data']['sets_by_theme_file'], delta)
        save_to_file(config_file['data']['sets_by_theme_file'], upserted_data)
    logging.debug("Script stopped!")


if __name__ == "__main__":
    main()
