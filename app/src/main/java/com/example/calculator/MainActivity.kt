package com.example.calculator

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mSimpleCalc = findViewById<Button>(R.id.simpleCalc)
        val mscientificCalc = findViewById<Button>(R.id.scientificCalc)
        val mAbout = findViewById<Button>(R.id.about)
        val mExit = findViewById<Button>(R.id.exit)

        mSimpleCalc.setOnClickListener {
            val intent = Intent(this, CalculatorSimple::class.java)
            startActivity(intent)
        }

        mscientificCalc.setOnClickListener {
            val intent = Intent(this, CalculatorScientific::class.java)
            startActivity(intent)
        }

        mAbout.setOnClickListener {
            val intent = Intent(this, About::class.java)
            startActivity(intent)
        }

        mExit.setOnClickListener {
            finish();
            exitProcess(0);
        }
    }
}