package com.foa.pos.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foa.pos.R;
import com.foa.pos.entity.Order;
import com.foa.pos.entity.Promotion;
import com.foa.pos.entity.Topping;

import java.util.List;

public class PromotionListAdapter extends RecyclerView.Adapter<PromotionListAdapter.ViewHolder>{

    private List<Promotion> dtList;
    private Activity context;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;


    public PromotionListAdapter(Activity context, List<Promotion> data) {
        this.context = context;
        this.dtList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = inflater.inflate(R.layout.card_item_promotion,null);;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Promotion promotion = dtList.get(position);
        holder.promotionCode.setText(promotion.getCode());
        holder.promotionExp.setText(promotion.getExp());
        holder.promotionRest.setText(String.valueOf(promotion.getRest()));
        holder.promotionDesc.setText(promotion.getDesc());

        if(promotion.isSelected()){
            holder.cardPromotion.setSelected(true);
        }else{
            holder.cardPromotion.setSelected(false);
        }

        holder.setItemClickListener((view, position1, isLongClick) -> {
            view.setSelected(true);
            if (promotion.isSelected()){
                promotion.setSelected(false);
                onItemClickListener.onRemove(promotion);
            }else {
                promotion.setSelected(true);
                if (onItemClickListener!=null){
                    onItemClickListener.onPick(promotion);
                }
            }
            checkHasSelectedItem(promotion);
            notifyDataSetChanged();
        });
        holder.itemView.setTag(promotion);
    }


    @Override
    public int getItemCount() {
        return dtList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout cardPromotion;
        TextView promotionCode;
        TextView promotionExp;
        TextView promotionRest;
        TextView promotionDesc;
        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardPromotion = itemView.findViewById(R.id.cardPromotion);
            promotionCode = itemView.findViewById(R.id.promotionCode);
            promotionExp = itemView.findViewById(R.id.promotionExp);
            promotionRest = itemView.findViewById(R.id.promotionRest);
            promotionDesc = itemView.findViewById(R.id.promotionDesc);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }
    }

    private boolean checkHasSelectedItem(Promotion item){
        for (int i = 0; i < dtList.size(); i++) {
            if(item.getPromotionId()!= dtList.get(i).getPromotionId() && dtList.get(i).isSelected()){
                dtList.get(i).setSelected(false);
                return true; //has item selected
            }
        }
        return false;// no item selected;
    }



    public interface ItemClickListener  {
        void onClick(View view, int position,boolean isLongClick);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener){
        this.onItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onPick(Promotion item);
        void onRemove(Promotion item);
    }
}
