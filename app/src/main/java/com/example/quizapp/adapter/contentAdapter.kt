package com.example.quizapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepolitan.quizapp.databinding.ItemContentBinding
import com.example.quizapp.model.Answers
import com.example.quizapp.model.Content

class contentAdapter: RecyclerView.Adapter<contentAdapter.ViewHolder>(){

    private var contents = mutableListOf<Content>()

    class ViewHolder (private  val  itemContentBinding: ItemContentBinding)
        : RecyclerView.ViewHolder(itemContentBinding.root){
        fun bindItem(content: Content) {


//       untuk menampilkan answer adapter ke content activity
            val answerAdapter = AnswerAdapter()

//            DATA DARI TVQUIZ
            itemContentBinding.tvQuiz.text = content.body

//            DATA GAMBAR DARI IMAGE VIEW
            if (content.image !=null && content.image.isNotEmpty()){
                itemContentBinding.ivQuiz.visibility = View.VISIBLE
                Glide.with(itemView)
                    .load(content.image)
                    .placeholder(android.R.color.darker_gray)
                    .into(itemContentBinding.ivQuiz)
            }else{
                itemContentBinding.ivQuiz.visibility =View.GONE
            }
            if (content.answers !=null){
                answerAdapter.setData(content.answers as MutableList<Answers>)

                itemContentBinding.rvAnswerQuiz.adapter = answerAdapter
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding = ItemContentBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.bindItem(contents[position])
    }

    override fun getItemCount(): Int = contents.size
        fun setData(contents: MutableList<Content>){
            this.contents = contents
            notifyDataSetChanged()
        }

    fun getResults(): MutableList<Content> {
    return  contents
    }
}
