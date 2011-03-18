#!/usr/bin/env python
import optparse
import subprocess

def main():
    p = optparse.OptionParser()
    p.add_option('--topology', '-t', default="TOPO", help="Name of the topology file.  Must be in the same directory as test.py.")
    options, arguments = p.parse_args()
    print '*** Start server and read topology file: ***'
    subprocess.Popen(['java', 'topology', 'TOPO'])
    print 'Output was '

if __name__ == '__main__':
    main()