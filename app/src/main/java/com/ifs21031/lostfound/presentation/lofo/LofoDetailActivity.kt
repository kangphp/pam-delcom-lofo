package com.ifs21031.lostfound.presentation.lofo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ifs21031.lostfound.data.helper.Utils.Companion.observeOnce
import com.ifs21031.lostfound.data.model.DelcomLofo
import com.ifs21031.lostfound.data.remote.MyResult
import com.ifs21031.lostfound.data.remote.response.LostFound
import com.ifs21031.lostfound.databinding.ActivityLofoDetailBinding
import com.ifs21031.lostfound.presentation.ViewModelFactory

class LofoDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLofoDetailBinding
    private val viewModel by viewModels<LofoViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var isChanged: Boolean = false
    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == LofoManageActivity.RESULT_CODE) {
            recreate()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLofoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupAction()
    }

    private fun setupView() {
        showComponent(false)
        showLoading(false)
    }

    private fun setupAction() {
        val todoId = intent.getIntExtra(KEY_TODO_ID, 0)
        if (todoId == 0) {
            finish()
            return
        }
        observeGetTodo(todoId)
        binding.appbarTodoDetail.setNavigationOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(KEY_IS_CHANGED, isChanged)
            setResult(RESULT_CODE, resultIntent)
            finishAfterTransition()
        }
    }

    private fun observeGetTodo(todoId: Int) {
        viewModel.getLofo(todoId).observeOnce { result ->
            when (result) {
                is MyResult.Loading -> {
                    showLoading(true)
                }

                is MyResult.Success -> {
                    showLoading(false)
                    loadTodo(result.data.data.lostFound)
                }

                is MyResult.Error -> {
                    Toast.makeText(
                        this@LofoDetailActivity,
                        result.error,
                        Toast.LENGTH_SHORT
                    ).show()
                    showLoading(false)
                    finishAfterTransition()
                }
            }
        }
    }

    private fun loadTodo(todo: LostFound) {
        showComponent(true)
        binding.apply {
            tvTodoDetailTitle.text = todo.title
            tvTodoDetailDate.text = "Dibuat pada: ${todo.createdAt}"
            tvTodoDetailDesc.text = todo.description
            cbTodoDetailIsFinished.isChecked = todo.isCompleted == 1
            cbTodoDetailIsFinished.setOnCheckedChangeListener { _, isChecked ->
                viewModel.putLofo(
                    todo.id,
                    todo.title,
                    todo.description,
                    isChecked
                ).observeOnce {
                    when (it) {
                        is MyResult.Error -> {
                            if (isChecked) {
                                Toast.makeText(
                                    this@LofoDetailActivity,
                                    "Gagal menyelesaikan todo: " + todo.title,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@LofoDetailActivity,
                                    "Gagal batal menyelesaikan todo: " + todo.title,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        is MyResult.Success -> {
                            if (isChecked) {
                                Toast.makeText(
                                    this@LofoDetailActivity,
                                    "Berhasil menyelesaikan todo: " + todo.title,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@LofoDetailActivity,
                                    "Berhasil batal menyelesaikan todo: " + todo.title,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            if ((todo.isCompleted == 1) != isChecked) {
                                isChanged = true
                            }
                        }

                        else -> {}
                    }
                }
            }
            ivTodoDetailActionDelete.setOnClickListener {
                val builder = AlertDialog.Builder(this@LofoDetailActivity)
                builder.setTitle("Konfirmasi Hapus Todo")
                    .setMessage("Anda yakin ingin menghapus todo ini?")
                builder.setPositiveButton("Ya") { _, _ ->
                    observeDeleteTodo(todo.id)
                }
                builder.setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss() // Menutup dialog
                }
                val dialog = builder.create()
                dialog.show()
            }
            ivTodoDetailActionEdit.setOnClickListener {
                val delcomTodo = DelcomLofo(
                    todo.id,
                    todo.title,
                    todo.description,
                    todo.isCompleted == 1,
                    todo.cover
                )
                val intent = Intent(
                    this@LofoDetailActivity,
                    LofoManageActivity::class.java
                )
                intent.putExtra(LofoManageActivity.KEY_IS_ADD, false)
                intent.putExtra(LofoManageActivity.KEY_TODO, delcomTodo)
                launcher.launch(intent)
            }
        }
    }

    private fun observeDeleteTodo(todoId: Int) {
        showComponent(false)
        showLoading(true)
        viewModel.deleteLofo(todoId).observeOnce {
            when (it) {
                is MyResult.Error -> {
                    showComponent(true)
                    showLoading(false)
                    Toast.makeText(
                        this@LofoDetailActivity,
                        "Gagal menghapus todo: ${it.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is MyResult.Success -> {
                    showLoading(false)
                    Toast.makeText(
                        this@LofoDetailActivity,
                        "Berhasil menghapus todo",
                        Toast.LENGTH_SHORT
                    ).show()
                    val resultIntent = Intent()
                    resultIntent.putExtra(KEY_IS_CHANGED, true)
                    setResult(RESULT_CODE, resultIntent)
                    finishAfterTransition()
                }

                else -> {}
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbTodoDetail.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showComponent(status: Boolean) {
        binding.llTodoDetail.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    companion object {
        const val KEY_TODO_ID = "todo_id"
        const val KEY_IS_CHANGED = "is_changed"
        const val RESULT_CODE = 1001
    }
}