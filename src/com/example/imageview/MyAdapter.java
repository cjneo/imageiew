package com.example.imageview;



import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	 static List<ImagePiece> myimagelist;
    private String data[]=null;
    private ImagePiece imagearray[]=null;
    private Context context=null;
    private LayoutInflater inflater=null;
    public final class griditemview{
    	public ImageView image;
    	public TextView text;
    	
    	 int clicknum ;
    }
    public MyAdapter(Context context,String[] data, ImagePiece[] imgId) {
        super();
        this.data = data;
        this.imagearray = imgId;
        this.context = context;
        
        inflater=LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imagearray.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    

    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
//        锟斤拷锟絟older锟皆硷拷holder锟斤拷锟斤拷锟斤拷tv锟斤拷img锟斤拷锟斤拷锟绞碉拷锟�
        griditemview myitem =null;
        if(convertView==null){
            myitem = new griditemview();
            
            convertView=inflater.inflate(R.layout.gridview_item, null);
            
          
            myitem.image=(ImageView) convertView.findViewById(R.id.gridview_img);
            
            convertView.setTag(myitem);
            
        }else{
            myitem=(griditemview) convertView.getTag();
            
        }
//        为holder锟叫碉拷tv锟斤拷img锟斤拷锟斤拷锟斤拷锟斤拷
  
       // myitem.image.setImageResource(imgId[position]);
     myitem.image.setImageBitmap(imagearray[position].bitmap);
     
    
        return convertView;
    }

}
