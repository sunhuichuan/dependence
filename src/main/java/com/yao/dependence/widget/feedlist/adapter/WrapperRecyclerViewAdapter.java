package com.yao.dependence.widget.feedlist.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yao.devsdk.adapter.AceRecyclerAdapter;
import com.yao.devsdk.adapter.IListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 包装了 Header 和 Footer 的 RecyclerView Adapter
 * @param <T>
 * @param <VH>
 */
public class WrapperRecyclerViewAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder>  implements IListAdapter<T> {
    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;

    /** key 就是 headerView 的 itemType */
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();

    private AceRecyclerAdapter<T,VH> mInnerAdapter;



    // header 和 footer 的holder
    class HeaderFooterHolder extends RecyclerView.ViewHolder{

        public HeaderFooterHolder(View itemView) {
            super(itemView);
        }

    }



    public WrapperRecyclerViewAdapter(AceRecyclerAdapter<T, VH> adapter) {
        mInnerAdapter = adapter;
    }




    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }


    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFootView(View view) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFootViews.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null){

            HeaderFooterHolder holder = new HeaderFooterHolder(mHeaderViews.get(viewType));
            return holder;

        } else if (mFootViews.get(viewType) != null){
            HeaderFooterHolder holder = new HeaderFooterHolder(mFootViews.get(viewType));
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            //header 或者 footer 类型，则不做BindView
            return;
        }
        mInnerAdapter.onBindViewHolder((VH)holder, position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }




    /****---------------- IListAdapter 接口的方法 ---------------------***/

    @Override
    public void add(T t) {
        mInnerAdapter.add(t);
    }

    @Override
    public void add(int index, T t) {
        mInnerAdapter.add(index,t);
    }

    @Override
    public void addAll(List<T> list) {
        mInnerAdapter.addAll(list);
    }

    @Override
    public void addAll(int index, List<T> list) {
        mInnerAdapter.addAll(index,list);
    }

    @Override
    public void addAllReversely(List<T> list) {
        mInnerAdapter.addAllReversely(list);
    }

    @Override
    public void addAllReversely(List<T> list, int index) {
        mInnerAdapter.addAllReversely(list,index);
    }

    @Override
    public void remove(int index) {
        mInnerAdapter.remove(index);
    }

    @Override
    public void remove(T t) {
        mInnerAdapter.remove(t);
    }

    @Override
    public void clear() {
        mInnerAdapter.clear();
    }

    @Override
    public void setList(List<T> list) {
        mInnerAdapter.setList(list);
    }

    @Override
    public ArrayList<T> getList() {

        return mInnerAdapter.getList();
    }

    @Override
    public int getCount() {
        return mInnerAdapter.getCount();
    }


}