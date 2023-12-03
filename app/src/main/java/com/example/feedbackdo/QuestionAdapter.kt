package com.example.feedbackdo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class QuestionAdapter(private val questionList: ArrayList<Question>): RecyclerView.Adapter<QuestionAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(
           R.layout.question_item,
           parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentQues = questionList[position]
        holder.questionText.text = currentQues.question
        holder.questionNumber.text = (position+1).toString()

        holder.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            currentQues.rating = rating
            questionList[position].rating = rating
        }
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val questionText: TextView = itemView.findViewById(R.id.tv_question)
        val ratingBar: RatingBar = itemView.findViewById(R.id.customRatingBar)
        val questionNumber : TextView = itemView.findViewById(R.id.tv_questionNumber)
    }
}
