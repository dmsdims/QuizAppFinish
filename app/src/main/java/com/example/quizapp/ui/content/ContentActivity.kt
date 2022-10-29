package com.example.quizapp.ui.content

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.codepolitan.quizapp.R
import com.codepolitan.quizapp.databinding.ActivityContentBinding
import com.example.quizapp.adapter.contentAdapter
import com.example.quizapp.model.Content
import com.example.quizapp.repository.Repository
import com.example.quizapp.ui.main.MainActivity
import com.example.quizapp.ui.score.ScoreActivity

class ContentActivity : AppCompatActivity() {

    companion object{
        const val  EXTRA_NICKNAME = "extra_nickname"
        const val  EXTRA_CONTENT = " extra_content"
    }

    private lateinit var contentBinding: ActivityContentBinding
    private lateinit var contentAdapter: contentAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var dataSize = 0
    private var currentPosition = 0
    private var nickname: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentBinding =ActivityContentBinding.inflate(layoutInflater)
        setContentView(contentBinding.root)

        //init
        contentAdapter = contentAdapter()

//        get data

        if (intent != null){
            nickname = intent.getStringExtra(EXTRA_NICKNAME)
        }
        if (savedInstanceState != null){
            nickname = savedInstanceState.getString(EXTRA_NICKNAME)
            val  contents = savedInstanceState.getParcelableArrayList<Content>(EXTRA_CONTENT)
            showDataContents(contents)
        }else{
//            get data from repository
            val contents = Repository.getDataContents(this)

//        show data content
            showDataContents(contents)
        }
//        fungsi onclick buat panah preview dan next
        onClick()
    }

//    tombol close diatas

    @SuppressLint("SuspiciousIndentation")
    private fun onClick() {
        contentBinding.btnBack.setOnClickListener{
            AlertDialog.Builder(this)
//                    ketik are you sure trus alt +enter trus extraxt to string
                .setTitle(getString(R.string.are_you_sure))
                .setMessage(getString(R.string.message_exit))
                .setPositiveButton("Yes"){dialog,_->
                    dialog.dismiss()
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                }
                .setNegativeButton(getString(R.string.no)){ dialog, _->
                    dialog.dismiss()
                }
                .show()
//           buttom panah kiri kanan yang digunakan untuk preview dan next
        }
        contentBinding.btnNextContent.setOnClickListener{
            if (currentPosition < dataSize -1){
                contentBinding.rvContent.smoothScrollToPosition(currentPosition + 1)
        }else{
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.are_you_sure))
                .setMessage(getString(R.string.massage_input_data))
                .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                    val contents = contentAdapter.getResults()
                    val totalQuiz = contents.size
//                    cara mendapatkan data yang benar dari quiz
                    var totalCorrectAnswer = 0
//                    looping data benar pada quiz dikotlin yang baru harus ada tanda serunya
                    for (content in contents){
                        for (answer in content.answers!!){
                            if (answer!!.isClick == true && answer!!.correctAnswer == true){
                                totalCorrectAnswer += 1
                            }
                        }
                    }
                  val totalScore = totalCorrectAnswer.toDouble() / totalQuiz * 100
                    startActivity(Intent(this, ScoreActivity::class.java))
                    ScoreActivity.EXTRA_NICNAME to nickname
                    ScoreActivity.EXTRA_SCORE to totalScore.toInt()

                    dialog.dismiss()
                }
                .setNegativeButton(getString(R.string.no)){dialog,_->
                    dialog.dismiss()
                }
                .show()
                }
        }
        contentBinding.btnPrevContent.setOnClickListener {
            contentBinding.rvContent.smoothScrollToPosition(currentPosition - 1)
        }
    }

    //alt enter pilih yang 1 parameter
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    outState.putString(EXTRA_NICKNAME,nickname)
    val contents = contentAdapter.getResults()
    outState.putParcelableArrayList(EXTRA_CONTENT, contents as ArrayList<Content>)
    }

    private fun showDataContents(contents: List<Content>?) {
//        kenapa tidak ditampilkan langsung dixlm contenactivity karenamau dimaksukan ke addonlistener
    layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = PagerSnapHelper()

        if (contents !=null){
            contentAdapter.setData(contents as MutableList<Content>)
        }
        snapHelper.attachToRecyclerView(contentBinding.rvContent)
        contentBinding.rvContent.layoutManager = layoutManager
        contentBinding.rvContent.adapter = contentAdapter

//        jumlah halaman diadapter
        dataSize = layoutManager.itemCount
//        pbcontent merupakan progres bar
        contentBinding.pbContent.max =dataSize -1

//        menampilkan inmdeks yang pertama
        var index = "${currentPosition +1}/ $dataSize"
        contentBinding.tvIndexContent.text = index

//        ketika scroll
        contentBinding.rvContent.addOnScrollListener(object : RecyclerView.OnScrollListener(){
//            pilih yang ada dx int dy
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentPosition = layoutManager.findFirstVisibleItemPosition()
                index = "${currentPosition + 1} / $dataSize"
            contentBinding.tvIndexContent.text = index
            contentBinding.pbContent.progress = currentPosition
            }
        })
        }
    }

