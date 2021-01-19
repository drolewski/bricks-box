import json
import logging
import logging.config
import math
import os.path
import re

import requests
import yaml
from bs4 import BeautifulSoup


def config():
    global config_file
    config_file_d = open("./config/brickset-scrapping.yaml")
    config_file = yaml.load(config_file_d, Loader=yaml.FullLoader)
    open(config_file['logging']['file'], "w").close()
    logging.basicConfig(handlers=[logging.FileHandler(config_file['logging']['file'], 'w', 'utf-8')],
                        level=logging.DEBUG,
                        format=config_file['logging']['format'])

    if not os.path.isfile(config_file['data']['endpoints_file']):
        open(config_file['data']['endpoints_file'], "w").close()
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
    return config_file_d


def get_page(url):
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
    if pieces is not None and pieces.parent.previous_sibling.getText() == 'Pieces':
        name = article.find("div", class_="meta").find("h1").find("a").getText()
        if "  " in name:
            name = name.split("  ")[1]
        year = article.find("a", class_="year").getText()
        tags = article.find("div", class_="tags").find_all("a")
        model_id = tags[0].getText()
        theme = tags[1].getText()
        img_link = article.find("img")["src"]
        pieces_url = pieces["href"]
        pieces_num = pieces.getText()
        data = {"model_id": model_id, "name": name, "theme": theme, "year": year, "img_link": img_link,
                "pieces_num": pieces_num, "pieces_url": pieces_url, "instructions": None}
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


def is_link_working(instruction_link):
    if get_page(instruction_link) is not None:
        logging.debug("Is link: {0} working: True".format(instruction_link))
        return True
    logging.debug("Is link: {0} working: False".format(instruction_link))
    return False


def update_endpoints(url):
    logging.debug("Update endpoints")
    page_html = get_page(url)
    soup = BeautifulSoup(page_html, 'html.parser')
    list_of_brickset_endpoints = soup.find(class_="addtags").find_all("option")
    json_file = open(config_file['data']['endpoints_file'], "r")
    endpoints = json.load(json_file)
    json_file.close()
    list_of_instructions_endpoints = []
    for endpoint in list_of_brickset_endpoints:
        if endpoint.has_attr("value"):
            endpoint_value = endpoint["value"]
            if re.match("/instructions/theme.*", endpoint_value):
                list_of_instructions_endpoints.append(endpoint_value)
    endpoints['instructions'] = list_of_instructions_endpoints

    save_to_file(config_file['data']['endpoints_file'], endpoints)
    return endpoints


def add_instructions(delta, url):
    endpoints_list = update_endpoints(url + "/instructions")
    instructions_dict = get_instructions_dict(endpoints_list["instructions"], url)
    updated_delta = update_instructions_in_delta(delta, instructions_dict)
    logging.debug("Return updated delta")
    return updated_delta


def get_instructions_of_single_page(page_soup, instructions_dict):
    tr = page_soup.find("table", class_="neattable").find("tbody").find_all("tr")
    for row in tr:
        a = row.find_all("td", attrs={'class': None})[0].find("a")
        model_id = row.find("td", class_="hideindialog hideintabs").getText()
        instruction_link = None
        if a.has_attr("href"):
            instruction_link = a["href"]
        if instruction_link is not None:
            if model_id in instructions_dict:
                links_list = instructions_dict[model_id]
                if instruction_link not in links_list and is_link_working(instruction_link):
                    links_list.append(instruction_link)
            else:
                if is_link_working(instruction_link):
                    instructions_dict[model_id] = [instruction_link]
    return instructions_dict


def get_instructions_dict(endpoints_list, url):
    instructions_dict = {}
    logging.debug("Get instructions dict")
    for endpoint in endpoints_list:
        page_html = get_page(url + endpoint)
        soup = BeautifulSoup(page_html, 'html.parser')
        if instructions_exist(soup):
            number_of_pages = get_number_of_pages(soup)
            if number_of_pages > 1:
                for i in range(1, number_of_pages + 1):
                    page_url = "{0}{1}/page-{2}".format(url, endpoint, i)
                    logging.debug("Get instructions from: {0}".format(page_url))
                    page_soup = get_soup_of_url(page_url)
                    instructions_dict = get_instructions_of_single_page(page_soup, instructions_dict)
            else:
                logging.debug("Get instructions from single page.")
                instructions_dict = get_instructions_of_single_page(soup, instructions_dict)
    return instructions_dict


def instructions_exist(soup):
    number_of_sets = soup.find("div", class_="resultsfilter").find("div", class_="results").getText()
    number_of_sets_match = re.match(r'[0-9]* to (.*) of (.*) match[es]?', number_of_sets)
    if int(number_of_sets_match.group(1)) > 0:
        logging.debug("Instructions exists")
        return True
    logging.debug("Instructions doesn\'t exists")
    return False


def update_instructions_in_delta(delta, instructions_dict):
    logging.debug("Add instructions to delta")
    for key, value in instructions_dict.items():
        for theme in delta:
            model = next(filter(lambda i: str(i["model_id"])[
                                          :str(i["model_id"]).index("-") if "-" in str(i["model_id"]) else len(
                                              i["model_id"])] == key, theme["list"]))
            if model:
                model["instructions"] = value
    return delta


def main():
    config_file_d = config()
    logging.debug("Script started!")
    api_url = "https://brickset.com"
    sets_page_html = get_page(api_url + "/sets")
    if sets_page_html is not None:
        logging.debug("Sets page exists.")
        endpoints_dict = get_dict_of_endpoints(sets_page_html)
        sets_by_theme = get_list_of_all_themes(api_url, endpoints_dict["theme"])
        delta = calculate_delta(config_file['data']['sets_by_theme_file'], sets_by_theme)
        updated_delta = add_instructions(delta, api_url)
        upserted_data = upsert_sets_by_theme(config_file['data']['sets_by_theme_file'], updated_delta)
        save_to_file(config_file['data']['sets_by_theme_file'], upserted_data)
    logging.debug("Script stopped!")
    config_file_d.close()


if __name__ == "__main__":
    main()
