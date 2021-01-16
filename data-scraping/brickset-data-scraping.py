import requests
from bs4 import BeautifulSoup
import re


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

    return {"theme": list_of_theme_endpoints, "year": list_of_year_endpoints}


def main():
    sets_page_html = get_sets_page("https://brickset.com/sets")
    if sets_page_html is not None:
        endpoints_dict = get_dict_of_endpoints(sets_page_html)


if __name__ == "__main__":
    main()
