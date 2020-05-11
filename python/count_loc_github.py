#!/usr/bin/env python
"""
Python Script is based on the following script: https://gist.github.com/ralphbean/5733076
"""

import pygithub3
from git import Repo
import subprocess
import os
import sys

gh = None
repositories = []
file_extensions=["html","php","py","c","cpp","cs"]

def gather_clone_urls(organization, no_forks=True):
    all_repos = gh.repos.list(user=organization).all()
    for repo in all_repos:

        # Don't print the urls for repos that are forks.
        if no_forks and repo.fork:
            continue

        yield repo.clone_url


if __name__ == '__main__':
    if len(sys.argv)!=2:
        print("You need to enter a user name of a GitHub account!")
        exit(1)
    user_name = sys.argv[1]
    print("Analyzing Github repositories of user {}.".format(user_name))
    clone_dir = os.path.join(os.getcwd(),"temp")
    if os.path.exists(clone_dir):
        subprocess.call(("rm -rf {}").format(clone_dir),shell=True)
    gh = pygithub3.Github()
    clone_urls = gather_clone_urls(user_name)
    for url in clone_urls:
        repos={
            "repos":url
        }
        total = 0
        Repo.clone_from(url, clone_dir)
        for extension in file_extensions:
            try:
                #loc = subprocess.check_output(("cd {} && find {} -type f -name '*." + extension + "' | xargs wc -l | grep 'total' | sed -e 's/\<total\>//g'").format(clone_dir,clone_dir),shell=True)
                loc = subprocess.check_output(("find {} -type f -name '*." + extension + "' | xargs wc -l").format(clone_dir,clone_dir),shell=True)
                loc = loc.split()
                if len(loc)==1:
                    loc = 0
                else: 
                    loc = int(loc[-2])
                total = total + loc
                repos[extension]=loc
            except subprocess.CalledProcessError as e:
                pass
                #print(("No file with extension: {}").format(extension))
                #raise RuntimeError("command '{}' return with error (code {}): {}".format(e.cmd, e.returncode, e.output))
        print("Repository {} analyzed...".format(url))
        repos["total"]=total
        repositories.append(repos)
        subprocess.call(("rm -rf {}").format(os.path.join(os.getcwd(),"temp")),shell=True)
    print("\n---------------------------------------------------------------------------\n")
    print("User account: {}\n".format(user_name))
    for repos in repositories:
        print("Statistics for Repository: {}".format(repos["repos"]))
        for extension in file_extensions:
            print(".{} files:\t\t\t {} LOCs".format(extension,repos[extension]))
        print("Total LOCs:\t\t\t {} LOCs".format(repos["total"]))
        print("")
    print("---------------------------------------------------------------------------\n")
    for extension in file_extensions:
        print("Total *.{} files:\t\t\t {} LOCs".format(extension,sum(x[extension] for x in repositories)))
    print("Total LOCs:\t\t\t {} LOCs".format(sum(x["total"] for x in repositories)))