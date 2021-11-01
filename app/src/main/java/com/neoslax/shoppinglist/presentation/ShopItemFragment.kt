package com.neoslax.shoppinglist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.neoslax.shoppinglist.R
import com.neoslax.shoppinglist.domain.ShopItem

class ShopItemFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item, container, false)
    }

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = DEFAULT_VAL_GET_ID

    private lateinit var onShopItemFragmentExit: OnShopItemFragmentExit
    private lateinit var etNameLayout: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCountLayout: TextInputLayout
    private lateinit var etCount: EditText
    private lateinit var submitButton: Button
    private lateinit var viewModel: ShopItemViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnShopItemFragmentExit) {
            onShopItemFragmentExit = context
        } else {
            throw RuntimeException("Activity is not implement OnShopItemFragmentExit")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
        Log.d("ShopItemFragment", "onCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        initViews(view)
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
        viewModel.getShopItemById(shopItemId)

        viewModel.shopItem.observe(viewLifecycleOwner) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }

        submitButton.setOnClickListener {
            val name = etName.text?.toString()
            val count = etCount.text?.toString()
            viewModel.editShopItem(name, count)
        }

    }

    private fun initViews(view: View) {
        etNameLayout = view.findViewById(R.id.editTextNameLayout)
        etCountLayout = view.findViewById(R.id.editTextNumberLayout)
        etName = view.findViewById(R.id.editTextName)
        etCount = view.findViewById(R.id.editTextNumber)
        submitButton = view.findViewById(R.id.submitButton)


    }

    private fun setupObserversVM() {
        viewModel.isFinished.observe(viewLifecycleOwner) {
            onShopItemFragmentExit.onShopItemFragmentExit()
        }

        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            if (it) {
                etCountLayout.error = getString(R.string.error_input_count)
            } else {
                etCountLayout.error = null
            }
        }
        viewModel.errorInputName.observe(viewLifecycleOwner) {
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

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("ScreenMode empty")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT_ITEM && mode != MODE_ADD_NEW_ITEM) {
            throw RuntimeException("ScreenMode not found")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT_ITEM) {
            if (!args.containsKey(ITEM_ID)) {
                throw RuntimeException("ShopItem ID not found")
            }
            shopItemId = args.getInt(ITEM_ID, DEFAULT_VAL_GET_ID)
        }
    }

    interface OnShopItemFragmentExit {
        fun onShopItemFragmentExit()
    }

    companion object {

        private const val SCREEN_MODE = "extra_screen_mode"
        private const val MODE_ADD_NEW_ITEM = "add_new_item"
        private const val MODE_EDIT_ITEM = "edit_item"
        private const val ITEM_ID = "edit_item_id"
        private const val DEFAULT_VAL_GET_ID = -1
        private const val MODE_UNKNOWN = ""

        fun getFragmentAddMode(): ShopItemFragment {

            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD_NEW_ITEM)
                }
            }
        }

        fun getFragmentEditMode(id: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT_ITEM)
                    putInt(ITEM_ID, id)
                }
            }
        }
    }
}