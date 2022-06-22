package com.example.ht5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ht5.adapter.HeroesAdapter
import com.example.ht5.model.Hero
import com.example.ht5.network.Repository

import java.lang.Thread.sleep

private lateinit var heroRecyclerAdapter: HeroesAdapter


interface OnItemClickListener{
    fun onItemClicked(position: Int, view: View)
}


class MainActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
       // val loading = findViewById<ProgressBar>(R.id.loading)

        val heroes = mutableListOf<Hero>()
        Repository().makeApiCall(heroes)
        println(heroes.size)

        while(heroes.size==0)
            sleep(100)


        val recyclerView = findViewById<RecyclerView>(R.id.heroesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        heroRecyclerAdapter = HeroesAdapter()
        heroRecyclerAdapter.setHeroList(heroes)
        recyclerView.adapter = heroRecyclerAdapter


        recyclerView.addOnItemCLickListener(object: OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {
                val clickedHero = heroRecyclerAdapter.getHeroByPos(position)
               // Toast.makeText(this@MainActivity, clickedHero.localized_name, Toast.LENGTH_SHORT).show()
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.containerForDetails, DetailsFragment(clickedHero))
//                    .commit()

                val intent = Intent(this@MainActivity, HeroDetailsActivity::class.java)
                intent.putExtra("name", clickedHero.localized_name)
                intent.putExtra("img", clickedHero.img)
                intent.putExtra("str", clickedHero.base_str.toString())
                intent.putExtra("strGain", clickedHero.str_gain.toString())
                intent.putExtra("int", clickedHero.base_int.toString())
                intent.putExtra("intGain", clickedHero.int_gain.toString())
                intent.putExtra("agi", clickedHero.base_agi.toString())
                intent.putExtra("agiGain", clickedHero.agi_gain.toString())
                intent.putExtra("attackType", clickedHero.attack_type)
                intent.putExtra("attr", clickedHero.primary_attr)

                startActivity(intent)
            }
        })
    }


    private fun RecyclerView.addOnItemCLickListener(onClickListener: OnItemClickListener){
        this.addOnChildAttachStateChangeListener(object: RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                view.setOnClickListener(null)
            }
            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                }
            }
        })
    }
}