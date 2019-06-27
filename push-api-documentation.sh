#!/bin/bash

USER=$1
EMAIL=$2
DOC_PATH=$3
FILE=$4
REPO=$5
GH_REPO=$6
MESSAGE=$7
TOKEN=$8

echo $MESSAGE

if grep -q $MESSAGE "[generate api-doc]";
then
    git clone git://$GH_REPO
    mv -f $DOC_PATH/$FILE $REPO
    cd $REPO
    git remote
    git config user.email $EMAIL
    git config user.name $USER
    git config user.email
    git config user.name
    git add $FILE
    git commit -m "$MESSAGE"
    git push "https://$TOKEN@$GH_REPO" master

    echo "Done!"

else
    echo "No push needed"
fi


