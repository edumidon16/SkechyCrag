package com.example.skechycrag.ui.add

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.skechycrag.R
import com.example.skechycrag.databinding.FragmentAddBinding
import com.example.skechycrag.ui.model.RouteInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val addViewModel: AddViewModel by viewModels()

    private var imageCapture: ImageCapture? = null
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(
                requireContext(),
                "Accept permission to access this feature",
                Toast.LENGTH_LONG
            )
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (checkCameraPermission()) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }

        binding.captureButton.setOnClickListener {
            takePhoto()
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                addViewModel.addState.collect { routeInfo ->
                    when (routeInfo) {
                        is AddState.Error -> errorState()
                        AddState.Loading -> loadingState()
                        AddState.Start -> startState()
                        is AddState.Success -> {
                            withContext(Dispatchers.Main) {
                                hideLoadingUI()
                                navigateToShowResponse(routeInfo.routeList)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun navigateToShowResponse(routeInfo: MutableList<RouteInfo>) {
        findNavController().navigate(
            R.id.action_addFragment2_to_showResponseFragment,
            bundleOf("routeInfo" to routeInfo)
        )
    }
    private fun startState() {

    }

    private fun loadingState() {
        showLoadingUI()
    }

    private fun errorState() {
        Toast.makeText(requireContext(), "It has been an error, please try again", Toast.LENGTH_LONG)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            // Initialize ImageCapture
            imageCapture = ImageCapture.Builder()
                .setTargetResolution(Size(1920, 1080)) // Example resolution, adjust based on device capabilities
                .build()

            // Add imageCapture to your camera provider
            try {
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (e: Exception) {
                Log.e("AddFragment", "Use case binding failed", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun checkCameraPermission(): Boolean {
        return PermissionChecker.checkSelfPermission(
            requireContext(), android.Manifest.permission.CAMERA
        ) == PermissionChecker.PERMISSION_GRANTED
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        // Create a ByteArrayOutputStream to hold the image data in memory
        val outputStream = ByteArrayOutputStream()

        // Prepare the image capture listener
        val outputOptions = ImageCapture.OutputFileOptions.Builder(outputStream).build()

        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val imageBytes = outputStream.toByteArray()
                    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                    // Convert bitmap to Base64 string
                    val base64ImageString = convertBitmapToBase64(bitmap)

                    addViewModel.readInfo(base64ImageString)
                }

                override fun onError(exc: ImageCaptureException) {
                    Log.e("AddFragment", "Photo capture failed: ${exc.message}", exc)
                }
            }
        )
    }

    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    private fun showLoadingUI() {
        binding.overlay.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE
        binding.captureButton.visibility = View.GONE
    }

    private fun hideLoadingUI() {
        binding.overlay.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }
}