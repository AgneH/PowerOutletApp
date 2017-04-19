package com.example.agneh.poweroutletapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Torkel on 19-Apr-17.
 */

public class CommentAdapter extends ArrayAdapter<Comment> {
        private ArrayList<Comment> dataSet;
        Context mContext;

        // View lookup cache
        private static class ViewHolder {
            TextView txtDate;
            TextView txtComment;
            ImageView imgUpvote;
        }

        public CommentAdapter(ArrayList<Comment> data, Context context) {
            super(context, R.layout.row_comment, data);
            this.dataSet = data;
            this.mContext=context;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Comment comment = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag

            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.row_comment, parent, false);
                viewHolder.txtComment = (TextView) convertView.findViewById(R.id.txtComment);
                viewHolder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
                viewHolder.imgUpvote = (ImageView) convertView.findViewById(R.id.imgUpvote);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            int picture = comment.getUpvote()==1 ? R.drawable.upvote : R.drawable.downvote;

            viewHolder.txtComment.setText(comment.getComment());
            viewHolder.txtDate.setText(comment.getDate());
            viewHolder.imgUpvote.setImageResource(picture);

            return convertView;
        }
}
