#!/usr/bin/env python
import optparse
import subprocess
import os

def main():
    p = optparse.OptionParser()
    p.add_option('--topology', '-t', default="TOPO", help="Name of the topology file.  Must be in the same directory as test.py.")
    options, arguments = p.parse_args()
    print '*** Start server and read topology file: ***'
    p1 = subprocess.Popen(['java', 'topology', options.topology], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    output, errors = p1.communicate()
    print output
    print errors

if __name__ == '__main__':
    main()