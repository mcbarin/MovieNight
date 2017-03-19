#!/usr/bin/python
# -*- coding: utf-8 -*-

import requests
from bs4 import BeautifulSoup
import json

def movie_details(movie):
    title = movie.find("a", {"class": "no_underline"}).text.replace("\n", "")
    image = movie.find("img").get("src")
    detail = movie.find("p").text.replace("\n", "")
    date = movie.findAll("div", {"class": "oflow_a"})[0].text.split("\n")[1]
    duration = movie.findAll("div", {"class": "oflow_a"})[0].text.split("\n")[2][1:-1]
    length = len(movie.findAll("div", {"class": "oflow_a"}))
    director = movie.findAll("div", {"class": "oflow_a"})[1].text.replace("\n", "") 
    genre = movie.findAll("div", {"class": "oflow_a"})[length-1].text.replace("\n", "")
    
    movie_dict = {"title": title, "image": image, "detail": detail, "date": date, "duration": duration, "director": director, "genre": genre}
    return movie_dict

def main():
    url = "http://www.beyazperde.com/filmler/vizyondakiler/sinema-sayisi/"
    request = requests.get(url)
    soup = BeautifulSoup(request.text, "lxml")
    movies = soup.findAll("div", {"class": "data_box"})
    movies_list = [movie_details(movie) for movie in movies]
    print("[", end="")
    for index in range(len(movies_list)):
        item = movies_list[index]
        if index == len(movies_list)-1:
            print(json.dumps(item,ensure_ascii=False), end="")
        else:
            print(json.dumps(item,ensure_ascii=False),", ",end="")
    print("]")

if __name__ == '__main__':
    main()
