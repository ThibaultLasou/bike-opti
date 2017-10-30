#!/usr/bin/python3
import json
import random
import sys
import math 

def createScenar(n, dataFile, resFolder):
    i = 0
    demandeScenar={}
    with open(dataFile) as data_file:    
        data = json.load(data_file)
        for s in data["main"]:
            i = s["number"]
            demandeScenar[i] = {}

            for j in range(0,s["available_bike_stands"]):
                dest = data["main"][random.randrange(len(data["main"]))]["number"]
                if dest not in demandeScenar[i].keys():
                    demandeScenar[i][dest] = 0
                nbVel = random.randrange(1,math.ceil(s["available_bike_stands"]/5)+1)
                (demandeScenar[i])[dest] += nbVel
                j += nbVel-1

    with open(resFolder+'scen0.json','w') as scenar_file:
        scenar_file.write(json.dumps(demandeScenar))


    for j in range(1,int(n)+1):
        demandeScenarJ = {}
        for u in demandeScenar:
            demandeScenarJ[u] = {}
            for d in demandeScenar[u]:
                demandeScenarJ[u][d] = demandeScenar[u][d] + random.randrange(math.floor(-0.2*demandeScenar[u][d]), math.ceil(0.2*demandeScenar[u][d]))
        with open(resFolder+'scen'+str(j)+'.json','w') as scenar_file:
            scenar_file.write(json.dumps(demandeScenarJ))


if __name__ == "__main__":
    createScenar(sys.argv[1], "assets/velib.json", "assets/scenarios/")
        

