#!/usr/bin/python
# -*- coding: utf-8 -*-

import requests
from bs4 import BeautifulSoup
import json

def movie_details(movie):
    title = movie.find("h2").text.strip()
    image = movie.find("img").get("data-src")
    detail = movie.find("div", {"class": "synopsis"}).text.strip()
    metaData = movie.find("div", {"class": "meta-body-item meta-body-info"})
    metaDict = extract_meta(metaData) 
    duration = metaDict["duration"]
    date = metaDict["date"]
    genre = metaDict["genre"]
    director = movie.find("div", {"class": "meta-body-item meta-body-direction light"}).a.text 
    
    movie_dict = {"title": title, "image": image, "detail": detail, "date": date, "duration": duration, "director": director, "genre": genre}
    return movie_dict

def extract_meta(metaData):
    spanArray = metaData.findAll("span")
    spacerCount = 0
    spacer = "/"
    defaultDuration = "Not Specified"
    for span in spanArray:
        if span.text == spacer:
            spacerCount += 1
    date = spanArray[0].text
    duration = defaultDuration
    genreArray = []
    genre = ""
    if spacerCount != 1:
        duration = metaData.contents[4].strip()
        for index in range(4, len(spanArray)):
            genreArray.append(spanArray[index].text)
        genre = ", ".join(genreArray)
    else:
        for index in range(2, len(spanArray)):
            genreArray.append(spanArray[index].text)
        genre = ", ".join(genreArray)
    
    return {"duration": duration, "date": date, "genre": genre}
    
        

def main():
    url = "http://www.beyazperde.com/filmler/vizyondakiler/sinema-sayisi/"
    request = requests.get(url)
    soup = BeautifulSoup(request.text, "lxml")
    movies = soup.findAll("div", {"class": "card card-entity card-entity-list cf hred"})
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
