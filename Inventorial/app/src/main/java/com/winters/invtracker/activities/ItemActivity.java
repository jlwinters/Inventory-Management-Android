package com.winters.invtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.view.ActionMode;
import android.graphics.Color;
import com.winters.invtracker.classes.Item;
import com.winters.invtracker.helpers.ItemDatabaseHelper;
import com.winters.invtracker.R;

public class ItemActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM = "com.winters.invtracker.item";
    private ItemDatabaseHelper mInvDb;
    private ItemAdapter mItemAdapter;
    private RecyclerView mRecyclerView;
    private int[] mItemColors;
    private Item mSelectedItem;
    private int mSelectedItemPosition = RecyclerView.NO_POSITION;
    private ActionMode mActionMode = null;
    ArrayList<Item> all_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        mItemColors = getResources().getIntArray(R.array.colorItem);

        // Singleton
        mInvDb = ItemDatabaseHelper.getInstance(getApplicationContext());

        mRecyclerView = findViewById(R.id.itemRecyclerView);


        // Create 4 grid layout columns
        RecyclerView.LayoutManager gridLayoutManager =
                new GridLayoutManager(getApplicationContext(), 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        // Shows the available subjects
        all_items = (ArrayList<Item>) mInvDb.getItems();
        mItemAdapter = new ItemAdapter(mInvDb.getItems());
        mRecyclerView.setAdapter(mItemAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Load subjects here in case settings changed
        mItemAdapter = new ItemAdapter(mInvDb.getItems());
        mRecyclerView.setAdapter(mItemAdapter);
    }


    public void addItemClick(View view) {
        // Prompt user to type new item
        Intent intent = new Intent(ItemActivity.this, ItemEditActivity.class);
        startActivity(intent);
    }

    private class ItemHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private Item mItem;
        private final TextView mTextView;
        private final TextView mItemQuantity;

        public ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recycler_view_items, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mTextView = itemView.findViewById(R.id.itemTextView);
            mItemQuantity = itemView.findViewById(R.id.itemQuantity);
        }

        public void bind(Item item, int position) {
            mItem = item;
            mTextView.setText(item.getText());
            mItemQuantity.setText(item.getQuantity());

            if (mSelectedItemPosition == position) {
                // Make selected card stand out
                mTextView.setBackgroundColor(Color.GRAY);
            } else {
                // Make the background color dependent on the length of the item string
                int colorIndex = item.getText().length() % mItemColors.length;
                mTextView.setBackgroundColor(mItemColors[colorIndex]);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (mActionMode != null) {
                return false;
            }

            mSelectedItem = mItem;
            mSelectedItemPosition = getBindingAdapterPosition();

            // Re-bind the selected item
            mItemAdapter.notifyItemChanged(mSelectedItemPosition);

            // Show the CAB
            mActionMode = ItemActivity.this.startActionMode(mActionModeCallback);

            return true;
        }

        @Override
        public void onClick(View view) {
            // FIXME: N/A on click, should allow quantity value modification
            Intent intent = new Intent(ItemActivity.this, ItemActivity.class);
            intent.putExtra(ItemActivity.EXTRA_ITEM, mItem.getText());
            startActivity(intent);
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

        private final List<Item> mItemList;

        public ItemAdapter(List<Item> items) {
            mItemList = items;
        }

        public void removeItem(Item item) {
            // Find item in the list
            int index = mItemList.indexOf(item);
            if (index >= 0) {
                // Remove the item
                mItemList.remove(index);

                // Notify adapter of item removal
                notifyItemRemoved(index);
            }
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position){
            holder.bind(mItemList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }
    }
    private final ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Provide context menu for CAB
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // Process action item selection
            if (item.getItemId() == R.id.delete) {
                // Delete from the database and remove from the RecyclerView
                mInvDb.deleteItem(mSelectedItem);
                mItemAdapter.removeItem(mSelectedItem);

                // Close the CAB
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;

            // CAB closing, need to deselect item if not deleted
            mItemAdapter.notifyItemChanged(mSelectedItemPosition);
            mSelectedItemPosition = RecyclerView.NO_POSITION;
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(ItemActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}