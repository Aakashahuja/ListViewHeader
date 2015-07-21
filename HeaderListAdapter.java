package com.osahub.rachit.listviewheader;

import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

public class HeaderListAdapter extends BaseAdapter {
    public final Map<String, ItemAdapter> sections = new LinkedHashMap<>();
    public final ArrayAdapter<String> headers;
    public final static int TYPE_SECTION_HEADER = 0;

    public HeaderListAdapter(Context context) {
        headers = new ArrayAdapter<>(context, R.layout.header);
    }

    // A section contains a Header and a list of Pojo objects that have been designed based on the Custom ItemAdapter.
    public void addSection(String section, ItemAdapter adapter) {
        this.headers.add(section);
        this.sections.put(section, adapter);
    }

    // Returns the title of the List Item that is at the specified position.
    public String getItem(int position) {
        // A loop over all sections - This is known as a for-each loop. It loops over the entire contents of the right size list.
        for (String section : this.sections.keySet()) {
            // Get the adapter for this section
            ItemAdapter adapter = sections.get(section);
            // Calculate the total number of items in this section. getCount method is defined below.
            int size = adapter.getCount() + 1;

            // Check if position is inside this section
            if (position == 0) return section;
            if (position < size) return adapter.getItem(position - 1).getTitle();

            // Otherwise jump into next section
            position -= size;
        }
        return null;
    }

    public int getCount() {
        // Total together all sections, plus one for each section header
        int total = 0;
        for (Adapter adapter : this.sections.values())
            total += adapter.getCount() + 1;
        return total;
    }

    @Override
    public int getViewTypeCount() {
        // Assuming that headers count as one, then total all sections
        int total = 1;
        for (Adapter adapter : this.sections.values())
            total += adapter.getViewTypeCount();
        return total;
    }

    // Return whether Header is clicked or listItem. This is useful for the next method.
    @Override
    public int getItemViewType(int position) {
        int type = 1;
        for (Object section : this.sections.keySet()) {
            Adapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            // Check if position inside this section
            if (position == 0) return TYPE_SECTION_HEADER;
            if (position < size) return type + adapter.getItemViewType(position - 1);

            // Otherwise jump into next section
            position -= size;
            type += adapter.getViewTypeCount();
        }
        return -1;
    }

    // This method will disable clicks on the Headers. But
    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) != TYPE_SECTION_HEADER);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int sectionnum = 0;
        for (Object section : this.sections.keySet()) {
            Adapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            // check if position inside this section
            if (position == 0) return headers.getView(sectionnum, convertView, parent);
            if (position < size) return adapter.getView(position - 1, convertView, parent);

            // otherwise jump into next section
            position -= size;
            sectionnum++;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}