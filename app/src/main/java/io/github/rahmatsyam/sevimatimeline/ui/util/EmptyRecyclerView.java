package io.github.rahmatsyam.sevimatimeline.ui.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class EmptyRecyclerView extends RecyclerView {
    private View emptyView;

    public EmptyRecyclerView(Context context){
        super(context);
    }

    public EmptyRecyclerView(Context context, AttributeSet  attrs){
        super(context, attrs);
    }

    public EmptyRecyclerView(Context context, AttributeSet  attrs, int defStyle){
        super(context, attrs, defStyle);
    }

    private void initEmptyView(){
        if(emptyView != null){
            emptyView.setVisibility(getAdapter() == null || getAdapter().getItemCount() == 0 ?
                    VISIBLE : GONE);
            EmptyRecyclerView.this.setVisibility( getAdapter() == null || getAdapter().getItemCount() == 0 ?
                    GONE : VISIBLE);
        }
    }

    final AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            initEmptyView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            initEmptyView();
            notify();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            initEmptyView();
            notify();
        }


    };

    @Override
    public void setAdapter(Adapter adapter){
        Adapter oldAdapter = getAdapter();
        super.setAdapter(adapter);

        if(oldAdapter != null){
            oldAdapter.unregisterAdapterDataObserver(observer);
        }

        if (adapter != null){
            adapter.registerAdapterDataObserver(observer);
        }
    }

    public void setEmptyView(View view){
        this.emptyView = view;
        initEmptyView();
    }


}
