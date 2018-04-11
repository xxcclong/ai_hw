from pypinyin import lazy_pinyin
import json
file_in = open('sina/2016-11','r')
han_list = json.load(file_in)
out_list = []
file12 = open("yierji.txt",'r')
all_str = file12.read()
for news in han_list:
    out_dict = {}
    to_be_list =''
    for hanzi in news:
        if hanzi in all_str:
            to_be_list = to_be_list + hanzi
        else:
            if hanzi ==' ':
                continue
            if len(to_be_list) == 0:
                continue
            out_dict['han'] = to_be_list
            out_dict['pinyin'] = lazy_pinyin(to_be_list)
            out_list.append(out_dict)
            to_be_list = ''
            out_dict = {}
# use the file
file_out = open('judge11111111111.json','w')
json.dump(out_list, file_out)
file_in.close()
file_out.close()

