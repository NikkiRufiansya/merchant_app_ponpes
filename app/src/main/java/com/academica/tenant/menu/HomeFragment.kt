package com.academica.tenant.menu

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.academica.tenant.R
import com.academica.tenant.adapter.PenjualanAdapter
import com.academica.tenant.core.helper.Connection
import com.academica.tenant.core.helper.Currency
import com.academica.tenant.core.helper.SessionManager
import com.academica.tenant.core.service.AuthService
import com.academica.tenant.core.service.HistoryService
import com.academica.tenant.core.service.KasirService
import com.academica.tenant.databinding.DialogPinBinding
import com.academica.tenant.databinding.FragmentHomeBinding
import com.academica.tenant.databinding.LayoutCameraBinding
import com.academica.tenant.models.PenjualanModels
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Base64.getEncoder


class HomeFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentHomeBinding
    lateinit var codeScanner: CodeScanner
    lateinit var dialogCameraBinding: LayoutCameraBinding
    lateinit var sessionManager: SessionManager
    lateinit var connection: Connection
    lateinit var authService: AuthService
    lateinit var historyService: HistoryService
    lateinit var kasirService: KasirService
    var penjualanModels: ArrayList<PenjualanModels> = ArrayList()
    var siswa_id = ""
    lateinit var dialog : BottomSheetDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        dialogCameraBinding = LayoutCameraBinding.inflate(LayoutInflater.from(requireContext()))
        codeScanner = CodeScanner(requireContext(), dialogCameraBinding.scannerView)
        sessionManager = SessionManager(requireContext())
        connection = Connection(requireContext())
        authService = AuthService.create(requireActivity())
        historyService = HistoryService.create(requireActivity())
        kasirService = KasirService.create(requireActivity())
        binding.swipe.setOnRefreshListener {
            getData()
            getProfile()
        }
        getData()
        getProfile()
        setOnClickListener()
        return binding.root
    }

    fun getProfile(){
        authService.getProfile(sessionManager.getId()).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var responseBody = response.body()?.string()
                var jsonObject = JSONObject(responseBody)
                if (jsonObject.getBoolean("success")){
                    binding.tvName.text = "Nama : " + jsonObject.getString("name")
                    binding.tvTotalSaldo.text = "Total Saldo : Rp " + Currency.nominal(jsonObject.getString("saldo"))
                    binding.swipe.isRefreshing = false
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })
    }



    fun getData() {
        historyService.getHistoryPenjualan(sessionManager.getId()).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var responseBody = response.body()?.string()
                var jsonArray = JSONArray(responseBody)

                var number = 1
                if (jsonArray.length() > 0){
                    for (i in 0 until jsonArray.length()){
                        var item = jsonArray.getJSONObject(i)
                        var newData = PenjualanModels()
                        newData.no = number++.toString()
                        if (item.has("date_transaksi")) newData.date = item.getString("date_transaksi")
                        if (item.has("nama_siswa")) newData.nama = item.getString("nama_siswa")
                        if (item.has("credit")) newData.Jumlah = item.getString("credit")
                        penjualanModels.add(newData)
                    }

                    var layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
                    binding.rvPenjualan.layoutManager = layoutManager
                    var penjualanAdapter : PenjualanAdapter = PenjualanAdapter(penjualanModels)
                    binding.rvPenjualan.adapter = penjualanAdapter
                    binding.swipe.isRefreshing = false
                    penjualanAdapter.notifyDataSetChanged()
                }


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })
    }

    companion object {
        fun getInstance(): HomeFragment = HomeFragment()
        private const val CAMERA_PERMISSION_CODE = 100
        private const val STORAGE_PERMISSION_CODE = 101
    }

    fun setOnClickListener() {
        binding.btnPembayaran.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_pembayaran -> openCamera()
        }
    }

    fun openCamera() {
        var dialog = BottomSheetDialog(requireContext())
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.setContentView(dialogCameraBinding.root)
        dialog.behavior.isDraggable = true
        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        codeScanner.decodeCallback = DecodeCallback {
            activity?.runOnUiThread {
                dialog.dismiss()
                //Toast.makeText(requireContext(), "Id Siswa: ${it.text}", Toast.LENGTH_LONG).show()
                Log.d("TAG", "openCamera: -->" + it.text)
                siswa_id = it.text
                dialogPin()
            }

        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            activity?.runOnUiThread {
                Toast.makeText(
                    requireContext(), "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
        codeScanner.startPreview()
        dialog.show()
    }

    fun dialogPin(){
        dialog = BottomSheetDialog(requireContext(), R.style.NoBackgroundDialogTheme)
        var dialogBinding = DialogPinBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.setContentView(dialogBinding.root)
        dialog.behavior.isDraggable = true



        dialogBinding.btnBayar.setOnClickListener{
            pembayaran(siswa_id, dialogBinding.etNominal.text.toString(), sessionManager.getId(),dialogBinding.etPin.text.toString())

        }
        dialog.show()
    }


    fun pembayaran(siswa_id: String, amount: String, merchant_id: String, pin:String){
        try{
            val encodedString: String = java.util.Base64.getEncoder().encodeToString(pin.toByteArray())

            kasirService.pembayaran(siswa_id, amount, merchant_id, encodedString).enqueue(object : Callback<ResponseBody>{
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    var responseBody = response.body()?.string()
                    var jsonObject = JSONObject(responseBody)
                    if(jsonObject.getBoolean("success")){
                        Toast.makeText(requireContext(), "Pembayaran Berhasil", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }else{
                        Toast.makeText(requireContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                }
            })
        }catch (e: Exception){

        }

    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this@HomeFragment.requireContext(), permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
        } else {
            //Toast.makeText(this@HomeFragment.requireContext(), "Permission already granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@HomeFragment.requireContext(), "Camera Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@HomeFragment.requireContext(), "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@HomeFragment.requireContext(), "Storage Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@HomeFragment.requireContext(), "Storage Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}