import jieba
strqinghua = jieba.cut("今天的天气很好，我们来到了北京清华大学来学习",cut_all = True)
ss = ' '.join(strqinghua)
print(type(ss))
