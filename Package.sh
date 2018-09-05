#!/bin/bash
appname=("unex-pim-item" "unex-pim-inventory" "unex-pim-price" "unex-pim-solr")
appfile=("docker/Dockerfile-pim" "docker/Dockerfile-pim-inventory" "docker/Dockerfile-pim-price" "docker/Dockerfile-pim-solr")
image_repo="harbor.baozun.com/unex"
image_repo_pre="ic-harbor.baozun.com/unex"
echo ${GIT_COMMIT:0:7}
echo ${GIT_BRANCH}


function  Package(){
if [ "${GIT_BRANCH}" ==  "master" ];then
    mvn  clean package -Dmaven.test.skip=true
    appname_len=${#appname[@]}
    for ((i = 0; i < appname_len; i++))
    do
        echo ${appname[$i]} ${appfile[$i]}
        docker images  --filter='reference=${image_repo}/${appname[$i]}:*' -q | xargs --no-run-if-empty docker rmi --force
        docker build -t ${image_repo}/${appname[$i]}:${GIT_COMMIT:0:7} -f  ${appfile[$i]} ./
    done
else
    appname_len=${#appname[@]}
    mvn  clean package -Dmaven.test.skip=true
    for ((i = 0; i <  appname_len; i++))
    do
        echo ${appname[$i]} ${appfile[$i]}
        docker images  --filter='reference=${image_repo_pre}/${appname[$i]}:*' -q | xargs --no-run-if-empty docker rmi --force
        docker build -t ${image_repo_pre}/${appname[$i]}:${GIT_COMMIT:0:7} -f  ${appfile[$i]} ./
    done
fi
}
function Publish(){
if [ "${GIT_BRANCH}" ==  "master" ];then
    appname_len=${#appname[@]}
    for ((i = 0; i < appname_len; i++))
    do
        echo ${appname[$i]} ${appfile[$i]}
        docker push ${image_repo}/${appname[$i]}:${GIT_COMMIT:0:7}
    done
else
    appname_len=${#appname[@]}
    for ((i = 0; i < appname_len; i++))
    do
        echo ${appname[$i]} ${appfile[$i]}
        docker push ${image_repo_pre}/${appname[$i]}:${GIT_COMMIT:0:7}
    done
fi
}

if [ $1 == "Package" ];then
     Package
elif [ $1 == "Publish" ];then
    Publish
fi