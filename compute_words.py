import json
import jieba
i = 1
all_dict = {}
two_dict = {}
while i<12:
    F = open("sina/2016-%02d"%(i),'r')
    i = i + 1
    news_list = json.load(F)
    for news in news_list:
        news_cut = jieba.cut(news, cut_all = True)
        ss = ' '.join(news_cut)
        for item in ss.split(' '):
            if item in all_dict:
                all_dict[item] = all_dict[item] + 1
            else:
                all_dict[item] = 1
    F.close()
write_file1 = open('words_model.json','w')
json.dump(all_dict, write_file1)
write_file1.close()
