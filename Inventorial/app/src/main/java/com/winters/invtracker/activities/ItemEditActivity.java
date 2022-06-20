package com.winters.invtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.winters.invtracker.R;
import com.winters.invtracker.classes.Item;
import com.winters.invtracker.helpers.ItemDatabaseHelper;

public class ItemEditActivity extends AppCompatActivity {

    ImageButton increment, decrement;
    EditText item_text, item_quantity;
    Button add_item_button;
    Boolean empty_place;
    String text_temp, quantity_temp;
    ItemDatabaseHelper mInvDb;
    int id_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        item_text = findViewById(R.id.editTextItemName);
        increment = findViewById(R.id.itemQtyIncrease);
        decrement = findViewById(R.id.itemQtyDecrease);
        item_quantity = findViewById(R.id.editTextItemQuantity);
        add_item_button = findViewById(R.id.addItemButton);
        mInvDb = new ItemDatabaseHelper(this);

        // Increment/increase button click listener
        increment.setOnClickListener(view -> {
            int input = 0, total;
            String value = item_quantity.getText().toString().trim();
            if (!value.isEmpty()) {
                input = Integer.parseInt(value);
            }
            total = input + 1;
            item_quantity.setText(String.valueOf(total));
        });

        add_item_button.setOnClickListener(view -> insertItemEntryToDB());

        // Decrement/decrease button click listener
        decrement.setOnClickListener(view -> {
            int input, total;
            String quantity = item_quantity.getText().toString().trim();
            if (quantity.equals("0")) {
                Toast.makeText(this, "Quantity reached zero", Toast.LENGTH_LONG).show();
            } else {
                input = Integer.parseInt(quantity);
                total = input - 1;
                item_quantity.setText(String.valueOf(total));
            }
        });
    }

    /**
     * Add item to database if name is valid
     */
    public void insertItemEntryToDB() {
        String message = CheckEditTextNotEmpty();
        if (!empty_place) {
            int id = id_temp;
            String text = text_temp;
            String quantity = quantity_temp;

            Item item = new Item(id, text, quantity);
            mInvDb.addItem(item);
            Toast.makeText(this,"Item added", Toast.LENGTH_LONG).show();

            Intent add = new Intent();
            setResult(RESULT_OK, add);
            this.finish();
        } else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Invalidate empty item name input
     */
    public String CheckEditTextNotEmpty() {
        String message = "";
        quantity_temp = item_quantity.getText().toString().trim();
        text_temp = item_text.getText().toString().trim();

        if (text_temp.isEmpty()) {
            item_text.requestFocus();
            empty_place = true;
            message = "Invalid item name";
        } else {
            empty_place = false;
        }
        return message;
    }
}