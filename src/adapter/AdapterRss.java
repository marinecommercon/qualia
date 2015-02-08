package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.qualia.R;
import dao.Rss;

import java.util.List;

public class AdapterRss extends BaseAdapter {

    List<Rss> listRss;
    LayoutInflater inflater;

    public AdapterRss(Context context, List<Rss> listRss) {

        inflater = LayoutInflater.from(context);
        this.listRss = listRss;

    }

    @Override
    public int getCount() {
        return listRss.size();
    }

    @Override
    public Object getItem(int position) {
        return listRss.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listRss.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_item, null);
            holder.rss_title = (TextView) convertView.findViewById(R.id.adapter_title);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.rss_title.setText(listRss.get(position).getName());

        return convertView;

    }

    private class ViewHolder {
        TextView rss_title;
    }

}
