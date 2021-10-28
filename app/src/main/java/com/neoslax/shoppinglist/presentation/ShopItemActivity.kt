package com.neoslax.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.neoslax.shoppinglist.R

class ShopItemActivity : AppCompatActivity() {

    private lateinit var etNameLayout: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCountLayout: TextInputLayout
    private lateinit var etCount: EditText
    private lateinit var submitButton: Button
    private lateinit var viewModel: ShopItemViewModel


    private var screenMode = MODE_UNKNOWN
    private var id = DEFAULT_VAL_GET_ID


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        initViews()
        setupEditTextListeners()
        setupObserversVM()

        when (screenMode) {
            MODE_ADD_NEW_ITEM -> launchAddMode()
            MODE_EDIT_ITEM -> launchEditMode()
        }


    }

    private fun launchAddMode() {


        submitButton.setOnClickListener {
            val name = etName.text?.toString()
            val count = etCount.text?.toString()
            viewModel.addShopItem(name, count)
        }


    }

    private fun launchEditMode() {

        viewModel.getShopItemById(id)

        viewModel.shopItem.observe(this) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }

        submitButton.setOnClickListener {
            val name = etName.text?.toString()
            val count = etCount.text?.toString()
            viewModel.editShopItem(name, count)
        }

    }

    private fun initViews() {
        etNameLayout = findViewById(R.id.editTextNameLayout)
        etCountLayout = findViewById(R.id.editTextNumberLayout)
        etName = findViewById(R.id.editTextName)
        etCount = findViewById(R.id.editTextNumber)
        submitButton = findViewById(R.id.submitButton)


    }

    private fun setupObserversVM() {
        viewModel.isFinished.observe(this) {
            finish()
        }

        viewModel.errorInputCount.observe(this) {
            if (it) {
                etCountLayout.error = getString(R.string.error_input_count)
            } else {
                etCountLayout.error = null
            }
        }
        viewModel.errorInputName.observe(this) {
            if (it) {
                etNameLayout.error = getString(R.string.error_input_name)
            } else {
                etNameLayout.error = null
            }
        }
    }

    private fun setupEditTextListeners() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("ScreenMode empty")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_EDIT_ITEM && mode != MODE_ADD_NEW_ITEM) {
            throw RuntimeException("ScreenMode not found")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT_ITEM) {
            if (!intent.hasExtra(ITEM_ID)) {
                throw RuntimeException("ShopItem ID not found")
            }
            id = intent.getIntExtra(ITEM_ID, DEFAULT_VAL_GET_ID)
        }
    }

    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_screen_mode"
        private const val MODE_ADD_NEW_ITEM = "add_new_item"
        private const val MODE_EDIT_ITEM = "edit_item"
        private const val ITEM_ID = "edit_item_id"
        private const val DEFAULT_VAL_GET_ID = -1
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD_NEW_ITEM)
            return intent
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT_ITEM)
            intent.putExtra(ITEM_ID, shopItemId)
            return intent
        }
    }
}