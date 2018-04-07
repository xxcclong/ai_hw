import json
file_list = []
i = 1
all_dict = {}
two_dict = {}
file12 = open("../pingyin12710172/yierji.txt",'r')
all_str = file12.read()
while i<12:
    F = open("sina/2016-%02d"%(i),'r')
    i = i + 1
    news_list = json.load(F)
    for news in news_list:
        former_one = ''
        for hanzi in news:
            if hanzi not in all_str:
                former_one = ''
                continue
            if hanzi in all_dict:
                all_dict[hanzi] = all_dict[hanzi] + 1
            else:
                all_dict[hanzi] = 1
            if former_one != '':
                two_model = former_one + hanzi
                if two_model in two_dict:
                    two_dict[two_model] = two_dict[two_model] + 1
                else:
                   two_dict[two_model] = 1 
            former_one = hanzi
    F.close()
write_file1 = open('one_model.json','w')
json.dump(all_dict, write_file1)
write_file1.close()
write_file2 = open('two_model.json','w')
json.dump(two_dict, write_file2)
write_file2.close()
