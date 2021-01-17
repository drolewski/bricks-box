import requests
from bs4 import BeautifulSoup
import re
import math
import logging
import logging.config


def config_logging():
    # TODO better logging config
    # TODO add more loggsssss
    open('./log/example.log', "w").close()
    logging.basicConfig(filename='./log/example.log', level=logging.DEBUG,
                        format='%(asctime)s - %(levelname)s - %(message)s')


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
    # save result to file -> later database
    return endpoints


def get_list_of_single_theme_list(url):
    soup = get_soup_of_url(url)
    number_of_pages = get_number_of_pages(soup)
    single_them_list = []
    if number_of_pages > 1:
        for i in range(1, number_of_pages + 1):
            page_url = "{0}/page-{1}".format(url, i)
            page_soup = get_soup_of_url(page_url)
            single_them_list += get_list_of_sets_data(page_soup)
    else:
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
        theme_list = get_list_of_single_theme_list('{0}{1}'.format(api_url, url))
        if len(theme_list) > 0:
            new_theme["list"] = theme_list
            new_theme["name"] = theme_list[0]["theme"]
            list_of_all_themes.append(new_theme)
    return list_of_all_themes


def main():
    config_logging()
    logging.debug("Script started!")
    api_url = "https://brickset.com"
    sets_page_html = get_sets_page(api_url + "/sets")
    if sets_page_html is not None:
        endpoints_dict = get_dict_of_endpoints(sets_page_html)
        result = get_list_of_all_themes(api_url, endpoints_dict["theme"])
        print(result)

    logging.debug("Script stopped! [delta]")


if __name__ == "__main__":
    main()
