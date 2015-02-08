package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.qualia.R;
import dao.Link;

import java.util.List;

public class AdapterLink extends BaseAdapter {

    List<Link> listLink;
    LayoutInflater inflater;

    public AdapterLink(Context context, List<Link> listLink) {

        inflater = LayoutInflater.from(context);
        this.listLink = listLink;

    }

    @Override
    public int getCount() {
        return listLink.size();
    }

    @Override
    public Object getItem(int position) {
        return listLink.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listLink.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_item, null);
            holder.link_title = (TextView) convertView.findViewById(R.id.adapter_title);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.link_title.setText(listLink.get(position).getTitle());

        return convertView;

    }

    private class ViewHolder {
        TextView link_title;
    }

}
