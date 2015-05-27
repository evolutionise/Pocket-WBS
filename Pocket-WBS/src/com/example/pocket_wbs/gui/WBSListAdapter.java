package com.example.pocket_wbs.gui;

import java.util.LinkedList;
import java.util.List;

import com.example.pocket_wbs.R;
import com.example.pocket_wbs.ViewElementActivity;
import com.example.pocket_wbs.model.ProjectTree;
import com.example.pocket_wbs.model.WBSElement;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WBSListAdapter extends BaseExpandableListAdapter {
	
	final private LinkedList<WBSElement> groups;
	final private LinkedList<WBSElement>[] children;
	final private ProjectTree tree;
	
	public WBSListAdapter(ProjectTree tree) {
		this.tree = tree;
		this.groups = tree.getRootElement().getChildren();
		this.children = new LinkedList[this.groups.size()];
		fillChildrenArray();
	}
	
	private void fillChildrenArray() {
		for (int i = 0; i < this.groups.size(); i++) {
			WBSElement group = this.groups.get(i);
			this.children[i] = addToChildrenArray(group.getChildren());
		}
	}
	
	private LinkedList<WBSElement> addToChildrenArray(LinkedList<WBSElement> children) {
		LinkedList<WBSElement> result = new LinkedList<WBSElement>();
		for (int i = 0; i < children.size(); i++) {
			WBSElement child = children.get(i);
			result.add(child);
			result.addAll(addToChildrenArray(child.getChildren()));
		}
		return result;
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		LinkedList<WBSElement> result = this.children[groupPosition];
		return result.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.groups.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		LinkedList<WBSElement> result = this.children[groupPosition];
		return result.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return groupPosition*1000 + childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		final WBSElement element = (WBSElement)getGroup(groupPosition);
		return getView(element, convertView, parent);
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		final WBSElement element = (WBSElement)getChild(groupPosition, childPosition);
		return getView(element, convertView, parent);
	}
	
	private View getView(final WBSElement element, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.list_view_element_item, null);
		}
		TextView nameLabel = (TextView)convertView.findViewById(R.id.list_view_label);
		nameLabel.setText(element.getName());
		
		TextView indexLabel = (TextView)convertView.findViewById(R.id.list_view_label_index);
		indexLabel.setText(element.getElementKey());
		
		ImageView open = (ImageView)convertView.findViewById(R.id.list_view_open);
		open.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Context context = v.getContext();
				Intent intent = new Intent(context, ViewElementActivity.class);
				String key = element.getElementKey();
				intent.putExtra("com.example.pocket_wbs.PROJECT_TREE", tree);
				intent.putExtra("com.example.pocket_wbs.ELEMENT_KEY", key);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
