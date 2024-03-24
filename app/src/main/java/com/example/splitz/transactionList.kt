package com.example.splitz

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.firestore.FirebaseFirestore

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var mProgressDialog: Dialog
interface progresBarss {
    fun showProgressDialog(context: Context, text: String)
    fun hideProgressDialog()

}
class transactionList : Fragment(), progresBarss {
    // TODO: Rename and change types of parameters

    private var mProgressDialog: Dialog? = null
    private var param1: String? = null
    private var param2: String? = null

    val db = FirebaseFirestore.getInstance()
    private lateinit var allUserRecyclerView: RecyclerView
    private lateinit var allTransactionList: ArrayList<transactionListData>
    private lateinit var noExpText : LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        allTransactionList = arrayListOf()

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_transaction_list, container, false)

        val backTraceTransaction = view.findViewById<ImageView>(R.id.backTraceTransaction)
         noExpText = view.findViewById<LottieAnimationView>(R.id.noExpText)
        allUserRecyclerView = view.findViewById(R.id.allTransactionRecycler)
        allUserRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
       // allUserRecyclerView.setHasFixedSize(true)
        getTransactionData()

        backTraceTransaction.setOnClickListener {
            if (childFragmentManager.backStackEntryCount > 0) {
                childFragmentManager.popBackStack()
            } else {
                activity?.onBackPressed()
            }
        }

        return view
    }

    private fun getTransactionData() {
        showProgressDialog(
            requireContext(),
            resources.getString(R.string.please_click_back_again_to_exit)
        )
        val savedUsername = getUsername()

        db.collection("Transaction").get()
            .addOnSuccessListener { collection ->
                hideProgressDialog()
                val documents = collection.documents
                for (document in documents) {
                    if (document.get("Name") == savedUsername) {



                        val Transactions = document.toObject(transactionListData::class.java)
                        //Toast.makeText(requireContext(),"$Transactions", Toast.LENGTH_LONG).show()
                        //transactionList.add(Transactions!!)
                        Transactions?.let {
                            allTransactionList.add(it)


                        }

                    }

                }
                if(allTransactionList.isNotEmpty()) {
                    allUserRecyclerView.adapter = transactioListAdapter(allTransactionList)
                } else {
                    noExpText.visibility = View.VISIBLE
                }
            }
    }
    private fun getUsername(): String? {
        // Retrieve the shared preferences
        val prefs = activity?.getSharedPreferences(login.PREFS_NAME, Context.MODE_PRIVATE)

        // Retrieve the username from shared preferences using the key
        return prefs?.getString(login.KEY_USERNAME, null)
    }
    override fun showProgressDialog(context: Context, text: String) {
        mProgressDialog = Dialog(context)
        mProgressDialog?.setContentView(R.layout.dialog_progress)
        // Set progress text or any other configurations
        // mProgressDialog.tv_progress_text.text = text
        mProgressDialog?.show()
    }

    override fun hideProgressDialog() {
        mProgressDialog?.dismiss()
    }
}