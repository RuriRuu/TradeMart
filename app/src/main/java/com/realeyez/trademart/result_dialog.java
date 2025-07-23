package com.realeyez.trademart;

//THIS IS A COMPLETE PLACEHOLDER IF ANYONE WISHES TO DO IT PLEASE DO
//ILL EVENTUALLY FIGURE THIS OUT BUT NOT IN ANY REASONABLE TIMEFRAME
public class result_dialog {
    //idk how this sort of shit works so Im just gonna implement my idea and I leave it up to you to fill it in
    private void setResdialog(postClass,postID){

        private postclass;
        private String postitle;
        private String postdesc;
        private String postauthor;
        private String postcash;
        private String postimage;

        if (postClass == post){//GENERAL POST
            postitle = postdatarequest(title);
            postdesc = postdatarequest(desc);
            postauthor = postdatarequest(author);
            if (hasimages = yes){
                postimage = postdatarequest(image);//get first image from post
            }

            result_title = posttitle;
            result_desc = postdesc;
            result_auth = postauthor;
        } else if (postClass == service) {//SERVICE
            postitle = postdatarequest(title);
            postdesc = postdatarequest(desc);
            postauthor = postdatarequest(author);
            postcash = postdatarequest(cash);
            if (hasimages = yes){
                postimage = postdatarequest(image);//get first image from post
            }

            result_title = posttitle;
            result_desc = postdesc;
            result_auth = postauthor;
        } else if (postClass == job) {//JOB
            postitle = postdatarequest(title);
            postdesc = postdatarequest(desc);
            postauthor = postdatarequest(author);
            postcash = postdatarequest(cash);
            if (hasimages = yes){
                postimage = postdatarequest(image);//get first image from post
            }

            result_title = posttitle;
            result_desc = postdesc;
            result_auth = postauthor;
        } else if (postClass == user) {//USER
            postitle = postdatarequest(title);
            if (hasimages = yes){
                postimage = postdatarequest(image);//get first image from post
            }

            result_title = posttitle;
            result_desc = postdesc;
            result_auth = postauthor;
        } else {
            //insert some error return
        }
    }
}
