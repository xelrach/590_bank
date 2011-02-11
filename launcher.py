#!/usr/bin/env python

import sys
import os

class branch(object):
    name = ""
    gui_ip = ""
    gui_port = ""
    server_ip = ""
    server_port = ""

    def __str__(self):
        return "<Branch " + self.name + " GUI: " + self.gui_ip + ":" + self.gui_port + " Server: " + self.server_ip + ":" + self.server_port + ">"

if __name__ == "__main__":
    file = open(sys.argv[1],'r')
    inst = None
    branches = []
    for line in file:
        if len(line)<1:
            continue
        fields = line.split(' ')
        if fields[0] not in ["Client:", "Server:", "client:", "server:"]:
            if inst:
                branches.append(inst)
            inst = branch()
            inst.name = fields[0]
        elif fields[0] in ["Client:", "client"]:
            inst.gui_ip = fields[1]
            inst.gui_port = fields[2]
        elif fields[0] in ["Server:", "server"]:
            inst.server_ip = fields[1]
            inst.server_port = fields[2]
    if inst:
        branches.append(inst)
    for branch in branches:
#        print branch
        if (not branch.name) or len(branch.name)<1:
            continue
        if branch.gui_ip.strip() in ['127.0.0.1', 'localhost', 'LOCALHOST']:
            os.spawnlp(os.P_WAIT, 'java', 'java', 'Branch_GUI', '--name', branch.name, '--port', branch.gui_port, '--branch', 
                    branch.server_ip + ":" + branch.server_port)
        if branch.server_ip.strip() in ['127.0.0.1', 'localhost', 'LOCALHOST']:
            args = ['java', 'Branch_Server', '--name', branch.name, '--port', branch.server_port, '--gui', branch.gui_ip + ":" + branch.gui_port]
            for other_branch in branches:
                if branch!=other_branch and other_branch.name and len(other_branch.name)>0 and len(other_branch.server_ip)>0:
#                    print "Python: " + str(len(other_branch.name))
#                    print "Python: " + other_branch.name+":"+other_branch.server_ip+":"+other_branch.server_port
                    args.extend(["--branch",other_branch.name+":"+other_branch.server_ip+":"+other_branch.server_port])
            os.spawnvp(os.P_WAIT, 'java', args)
