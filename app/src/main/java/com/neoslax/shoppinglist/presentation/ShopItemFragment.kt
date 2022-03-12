package com.neoslax.shoppinglist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.neoslax.shoppinglist.databinding.FragmentItemBinding
import javax.inject.Inject

class ShopItemFragment : Fragment() {
    private var screenMode: String = MODE_UNKNOWN

    private var shopItemId: Int = DEFAULT_VAL_GET_ID
    private var _binding: FragmentItemBinding? = null

    private val binding: FragmentItemBinding
        get() = _binding ?: throw RuntimeException("FragmentItemBinding == null")
    private lateinit var onShopItemFragmentExit: OnShopItemFragmentExit

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ShopItemViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as ShopListApp).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setupEditTextListeners()
        setupObserversVM()

        when (screenMode) {
            MODE_ADD_NEW_ITEM -> launchAddMode()
            MODE_EDIT_ITEM -> launchEditMode()
        }

    }

    private fun launchAddMode() {

        binding.submitButton.setOnClickListener {

            val name = binding.editTextName.text?.toString()
            val count = binding.editTextNumber.text?.toString()
            viewModel.addShopItem(name, count)
        }

    }

    private fun launchEditMode() {
        viewModel.getShopItemById(shopItemId)

        binding.submitButton.setOnClickListener {
            val name = binding.editTextName.text?.toString()
            val count = binding.editTextNumber.text?.toString()
            viewModel.editShopItem(name, count)

        }

    }

    private fun setupObserversVM() {
        viewModel.isFinished.observe(viewLifecycleOwner) {
            onShopItemFragmentExit.onShopItemFragmentExit()
        }

    }

    private fun setupEditTextListeners() {
        binding.editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.editTextNumber.addTextChangedListener(object : TextWatcher {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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