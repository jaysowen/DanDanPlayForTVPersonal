/*
 * Copyright (C) 2016 Yaroslav Pronin <proninyaroslav@mail.ru>
 *
 * This file is part of LibreTorrent.
 *
 * LibreTorrent is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LibreTorrent is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LibreTorrent.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.seiko.torrent.ui.add;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.seiko.torrent.R;
import com.seiko.torrent.model.filetree.BencodeFileTree;
import com.seiko.torrent.model.filetree.FileNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/*
 * The adapter for representation of downloadable files in a file tree view.
 */

public class DownloadableFilesAdapter
        extends RecyclerView.Adapter<DownloadableFilesAdapter.ViewHolder>
{
    private final Context context;
    private final ViewHolder.ClickListener clickListener;
    private final LayoutInflater inflater;
    protected List<BencodeFileTree> files = new ArrayList<>();

    public DownloadableFilesAdapter(Context context, ViewHolder.ClickListener clickListener)
    {
        this.context = context;
        this.clickListener = clickListener;
        this.inflater = LayoutInflater.from(context);
    }

    public void setFiles(Collection<BencodeFileTree> list) {
        if (!files.isEmpty()) {
            files.clear();
        }
        files.addAll(list);
        Collections.sort(files);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = inflater.inflate(R.layout.torrent_item_torrent_downloadable_file, parent, false);
        return new ViewHolder(v, clickListener, files);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {
        final BencodeFileTree file = files.get(position);

        holder.fileName.setText(file.getName());

        if (file.getType() == FileNode.Type.DIR)
            holder.fileIcon.setImageResource(R.drawable.ic_folder_grey600_24dp);
        else if (file.getType() == FileNode.Type.FILE)
            holder.fileIcon.setImageResource(R.drawable.ic_file_grey600_24dp);

        if (file.getName().equals(BencodeFileTree.PARENT_DIR)) {
            holder.fileSelected.setVisibility(View.GONE);
            holder.fileSize.setVisibility(View.GONE);
        } else {
            holder.fileSelected.setVisibility(View.VISIBLE);
            holder.fileSelected.setChecked(file.isSelected());
            holder.fileSize.setVisibility(View.VISIBLE);
            holder.fileSize.setText(Formatter.formatFileSize(context, file.size()));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ClickListener listener;
        private List<BencodeFileTree> files;
        TextView fileName;
        TextView fileSize;
        ImageView fileIcon;
        CheckBox fileSelected;

        public ViewHolder(View itemView, final ClickListener listener, final List<BencodeFileTree> files)
        {
            super(itemView);

            this.listener = listener;
            this.files = files;
            itemView.setOnClickListener(this);

            fileName = itemView.findViewById(R.id.file_name);
            fileSize = itemView.findViewById(R.id.file_size);
            fileIcon = itemView.findViewById(R.id.file_icon);
            fileSelected = itemView.findViewById(R.id.file_selected);
            fileSelected.setOnClickListener((View v) -> {
                if (listener != null)
                    listener.onItemCheckedChanged(ViewHolder.this.files.get(getAdapterPosition()),
                                                  fileSelected.isChecked());
            });
        }

        @Override
        public void onClick(View v)
        {
            int position = getAdapterPosition();

            if (listener != null && position >= 0) {
                BencodeFileTree file = files.get(position);

                if (file.getType() == FileNode.Type.FILE) {
                    /* Check file if it clicked */
                    fileSelected.performClick();
                }

                listener.onItemClicked(file);
            }
        }

        public interface ClickListener
        {
            void onItemClicked(BencodeFileTree node);

            void onItemCheckedChanged(BencodeFileTree node, boolean selected);
        }
    }
}