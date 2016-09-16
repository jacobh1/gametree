#!/usr/bin/env python3
#import numpy as np
from pprint import pprint
import re

xml_regex='^(<.+?>)(.+?)(<.+?>)$'

def cmpr(x, y):
    return x==y

def isclose(x,y,tol=1e-6):
#    print('DIFF: {}'.format(abs(abs(x)-abs(y))))
    return abs(abs(x)-abs(y)) < tol

with open('diff_sample.txt') as f:
    comparisons={}
    for line in f:
        if re.match('^\d', line):
            line_nums=line.rstrip()
            key=line_nums
            print('new pair: {}'.format(line_nums))
        if re.match('^---', line):
#            print('pair splitter: {}'.format(line.rstrip()))
            pass
        if re.match('^<', line):
            xml=line[1:].strip()
            print('left compare: {}'.format(xml))
            comparisons[key]={}
            comparisons[key]['lt']= re.match(xml_regex, xml).groups()
#            (open_tag, content, close_tag) = re.match(xml_regex, xml).groups()
#            print('{}::{}::{}'.format(open_tag, content, close_tag))
        if re.match('^>', line):
            xml=line[1:].strip()
            comparisons[key]['rt']= re.match(xml_regex, xml).groups()
#            print('right compare: {}'.format(xml))
#            open_tag, content, close_tag = re.match(xml_regex, xml).groups()
#            print('{}::{}::{}'.format(open_tag, content, close_tag))
    pprint(comparisons)
    for k in comparisons:
        bools = list(map(cmpr, comparisons[k]['lt'], comparisons[k]['rt']))
#        print(bools)
#        print(list(map(type, comparisons[k]['lt'])))
#        print(list(map(type, comparisons[k]['rt'])))
        l_open_tag, l_content, l_close_tag=comparisons[k]['lt']
        r_open_tag, r_content, r_close_tag=comparisons[k]['rt']
#        print('numerical compare:: {}'.format(isclose(float(l_content), float(r_content))))
        overall_compare= l_open_tag==r_open_tag and l_close_tag==r_close_tag and isclose(float(l_content), float(r_content))
        print('{}: overall compare:: {}'.format(k, overall_compare))


