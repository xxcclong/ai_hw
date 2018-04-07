###

###
import json
dict_file = open('ping_han_dict.json','r')
Dict = json.load(dict_file)
news_file = open('../sina_news/2016-01.txt','r')
news = json.load(news_file)
# html title
print(type(news))