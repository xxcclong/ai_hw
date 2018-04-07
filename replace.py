import json
f = open('../sina_news/2016-11.txt','r')
s = f.readline()
Output = []
while(s):
    s = json.loads(s)
    Output.append(s['html'])
    Output.append(s['title'])
    s = f.readline()
f.close()
k = open('sina/2016-11','w')
json.dump(Output,k)

