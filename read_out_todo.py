import json
def concu_pos(w1, w2, t = 0.5): # 两个汉字 
    global model1, model2, word_model, ALL
    if (w1 + w2) in word_model:
        return 1 - (0.5) ** word_model[w1+w2]
    if (w1 + w2) in model2:
        fenzi = model2[w1 + w2]
    else: fenzi = 0
    if w1 not in model1:
        return 0
    fenmu = model1[w1]
    moreover = 0
    if w2 in model1:
        moreover = model1[w2] / ALL
    return (fenzi / fenmu)*t + moreover*(1-t)
file2 = open('two_model.json','r')
file1 = open('one_model.json','r')
file3 = open('words_model.json', 'r')
word_model = json.load(file3)
model1 = json.load(file1)
model2 = json.load(file2)
file1.close()
file2.close()
file_dict = open('ping_han_dict.json','r')
ping_han_dict = json.load(file_dict)
file_dict.close()
ALL = 0
for k in model1:
    ALL = ALL + model1[k]
while True:
    input_str = input()
    input_list = input_str.split(' ')
    first = input_list[0]
    max_all = 0
    Output = []
    for item in ping_han_dict[first]:
        former = item
        i = 1
        this_poss = 1
        thischuan = []
        thischuan.append(former)
        while i < len(input_list):
            all_hanzi = ping_han_dict[input_list[i]]
            max_j = ''
            max_pos = 0
            for j in all_hanzi:
                my_pos = concu_pos(former, j)
                if my_pos > max_pos:
                    max_pos = my_pos
                    max_j = j
            i = i + 1
            thischuan.append(max_j)
            former = max_j
            this_poss = this_poss * max_pos
            print(this_poss)
            print(thischuan)
        if this_poss > max_all:
            max_all = this_poss
            Output = thischuan
    ui = 0
    the_former = ''
    for item in Output:
        print(item)
        if ui == 0:
            the_former = item
            ui = 1
            continue
        if (the_former + item ) in model2:
            print(the_former + item + ' ' + str(model2[the_former+ item]))
        else:
            print("no " + the_former + item)
        the_former = item
    outstr = ''
    for item in Output:
        outstr = outstr + item
    print("**************"+outstr)
