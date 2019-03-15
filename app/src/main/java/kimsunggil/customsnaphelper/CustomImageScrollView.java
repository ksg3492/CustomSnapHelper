package kimsunggil.customsnaphelper;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomImageScrollView extends RelativeLayout {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SnapHelper snapHelper;
    private AdapterImageScroll adapterImageScroll;

    public CustomImageScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View v = inflate(context, R.layout.layout_image_scroll, this);

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView = v.findViewById(R.id.rv_horizontal);
        recyclerView.setLayoutManager(layoutManager);

        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void updateList(ArrayList<String> list) {
        adapterImageScroll = new AdapterImageScroll();
        recyclerView.setAdapter(adapterImageScroll);

        adapterImageScroll.updateList(list);
        adapterImageScroll.notifyDataSetChanged();
    }

    class AdapterImageScroll extends RecyclerView.Adapter<AdapterImageScroll.ImageScrollViewHolder> {
        private int defaultPadding = -1;

        private int highlightPosition = -1;
        private int prevHighlightPosition = -1;

        private final int MAX_HORIZONTAL_ITEM_SIZE = 5;

        private ArrayList<String> imageList = new ArrayList<>();

        public void updateList(ArrayList<String> list) {
            imageList.add("");
            imageList.add("");
            for (String s : list) {
                imageList.add(s);
            }
            imageList.add("");
            imageList.add("");
        }

        @NonNull
        @Override
        public AdapterImageScroll.ImageScrollViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_scroll, viewGroup, false);

            ViewGroup.LayoutParams params = v.getLayoutParams();
            params.width = getWidth() / MAX_HORIZONTAL_ITEM_SIZE;
            params.height = params.width;
            v.setLayoutParams(params);

            AdapterImageScroll.ImageScrollViewHolder holder = new AdapterImageScroll.ImageScrollViewHolder(v);

            if (defaultPadding == -1) {
                defaultPadding = v.getPaddingBottom();
            }

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterImageScroll.ImageScrollViewHolder viewHolder, int position) {
            if (position == 0 || position == 1 || position == imageList.size() - 1 || position == imageList.size() - 2) {
                viewHolder.iv.setVisibility(View.GONE);
            } else {
                int padding;
                if (highlightPosition == position) {
                    padding = 0;
                } else {
                    padding = defaultPadding;
                }
                viewHolder.itemView.setPadding(padding, padding, padding, padding);

                viewHolder.iv.setVisibility(View.VISIBLE);
                Glide.with(viewHolder.iv.getContext()).load(imageList.get(position)).error(new ColorDrawable(Color.BLACK)).into(viewHolder.iv);
            }
        }

        @Override
        public int getItemCount() {
            return imageList.size();
        }

        public void setHighlight(int position) {
            if (highlightPosition != position) {
                prevHighlightPosition = highlightPosition;
                highlightPosition = position;

                View highlightView = layoutManager.findViewByPosition(highlightPosition);

                int[] snapDistance = snapHelper.calculateDistanceToFinalSnap(layoutManager, highlightView);
                if (snapDistance[0] != 0 || snapDistance[1] != 0) {
                    recyclerView.smoothScrollBy(snapDistance[0], snapDistance[1]);
                }
                startAnimation(true, highlightView);

                //prev item decrease
                if (prevHighlightPosition != -1) {
                    View prevhighlightView = layoutManager.findViewByPosition(prevHighlightPosition);
                    if (prevhighlightView != null) {
                        startAnimation(false, prevhighlightView);
                    } else {
                        notifyDataSetChanged();
                    }
                }
            }
        }

        private void startAnimation(boolean increase, final View view) {
            ValueAnimator increaseAnimation = new ValueAnimator();
            increaseAnimation.setDuration(200);
            if (increase) {
                increaseAnimation.setIntValues(defaultPadding, 0);
            } else {
                increaseAnimation.setIntValues(0, defaultPadding);
            }
            increaseAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int value = Integer.parseInt(valueAnimator.getAnimatedValue().toString());
                    view.setPadding(value, value, value, value);
                }
            });
            increaseAnimation.start();
        }

        class ImageScrollViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
            public ImageView iv;

            public ImageScrollViewHolder(@NonNull View itemView) {
                super(itemView);
                iv = itemView.findViewById(R.id.iv);
                iv.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();

                if (position != -1) {
                    setHighlight(position);
                }
            }
        }
    }
}
