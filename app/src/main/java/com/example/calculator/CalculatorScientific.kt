package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

class CalculatorScientific : AppCompatActivity() {
    enum class MathOperation {
        ADDITION,
        SUBTRACTION,
        MULTIPLICATION,
        DIVISION,
        NONE
    }

    fun makeToast(vararg texts: String) {
        Toast.makeText(
            this@CalculatorScientific,
            texts.joinToString(separator = "\n"),
            Toast.LENGTH_LONG
        ).show()
    }

    fun addNumberToString(inputString: String, number: Number): String {
        val resultString = if (inputString != "0") {
            inputString + number.toString()
        } else {
            number.toString()
        }
        return resultString
    }

    fun equal(
        firstString: String,
        previewString: String,
        mFirstNumber: TextView,
        mPreview: TextView,
        operation: CalculatorSimple.MathOperation
    ): String {
        if (previewString.isEmpty()) {
            return ""
        }

        val firstNum = previewString.replace(',', '.').toBigDecimal()
        val secondNum = firstString.replace(',', '.').toBigDecimal()

        val s = if (operation == CalculatorSimple.MathOperation.DIVISION && secondNum == BigDecimal.ZERO) {
            makeToast("Nie dziel przez 0!")
            return "err"
        } else {
            val sum = when (operation) {
                CalculatorSimple.MathOperation.MULTIPLICATION -> firstNum * secondNum
                CalculatorSimple.MathOperation.ADDITION -> firstNum + secondNum
                CalculatorSimple.MathOperation.SUBTRACTION -> firstNum - secondNum
                CalculatorSimple.MathOperation.DIVISION -> firstNum.divide(secondNum, 8, RoundingMode.HALF_UP)
                    .stripTrailingZeros()
                CalculatorSimple.MathOperation.NONE -> return ""
            }
            val df = DecimalFormat("#.####################")
            df.format(sum.stripTrailingZeros()).toString().replace('.', ',')
        }

        mFirstNumber.text = s
        mPreview.text = ""
        return s
    }

    lateinit var mFirstNumber: TextView
    lateinit var mPreview: TextView
    var firstString: String = "0"
    var previewString: String = ""
    var operation: CalculatorSimple.MathOperation = CalculatorSimple.MathOperation.NONE
    var isComma: Boolean = false

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("firstString", firstString)
        outState.putString("previewString", previewString)
        outState.putBoolean("isComma", isComma)
        outState.putString("operation", operation.name)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        firstString = savedInstanceState.getString("firstString", "")
        previewString = savedInstanceState.getString("previewString", "")
        isComma = savedInstanceState.getBoolean("isComma", false)
        operation = CalculatorSimple.MathOperation.valueOf(savedInstanceState.getString("operation")!!)

        mFirstNumber = findViewById(R.id.mainNumber) ?: mFirstNumber
        mPreview = findViewById(R.id.preview) ?: mPreview

        mFirstNumber.text = firstString
        mPreview.text = previewString
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator_scientific)

        val mFirstNumber = findViewById<TextView>(R.id.mainNumber)
        val mPreview = findViewById<TextView>(R.id.preview)
        mFirstNumber.text = firstString
        mPreview.text = previewString

        val mButton0 = findViewById<Button>(R.id.button0)
        val mButton1 = findViewById<Button>(R.id.button1)
        val mButton2 = findViewById<Button>(R.id.button2)
        val mButton3 = findViewById<Button>(R.id.button3)
        val mButton4 = findViewById<Button>(R.id.button4)
        val mButton5 = findViewById<Button>(R.id.button5)
        val mButton6 = findViewById<Button>(R.id.button6)
        val mButton7 = findViewById<Button>(R.id.button7)
        val mButton8 = findViewById<Button>(R.id.button8)
        val mButton9 = findViewById<Button>(R.id.button9)
        val mButtonBksp = findViewById<Button>(R.id.buttonBksp)
        val mButtonAC = findViewById<Button>(R.id.buttonC)
        val mButtonComma = findViewById<Button>(R.id.buttonComma)
        val mButtonSign = findViewById<Button>(R.id.buttonSign)
        val mButtonPlus = findViewById<Button>(R.id.buttonPlus)
        val mButtonEqual = findViewById<Button>(R.id.buttonEqual)
        val mButtonSub = findViewById<Button>(R.id.buttonSub)
        val mButtonMul = findViewById<Button>(R.id.buttonMul)
        val mButtonDiv = findViewById<Button>(R.id.buttonDiv)

        mButton0.setOnClickListener {
            if (firstString != "0") firstString += "0"
            mFirstNumber.setText(firstString)
        }

        mButton1.setOnClickListener {
            firstString = addNumberToString(firstString, 1)
            mFirstNumber.setText(firstString)
        }

        mButton2.setOnClickListener {
            firstString = addNumberToString(firstString, 2)
            mFirstNumber.setText(firstString)
        }

        mButton3.setOnClickListener {
            firstString = addNumberToString(firstString, 3)
            mFirstNumber.setText(firstString)
        }

        mButton4.setOnClickListener {
            firstString = addNumberToString(firstString, 4)
            mFirstNumber.setText(firstString)
        }

        mButton5.setOnClickListener {
            firstString = addNumberToString(firstString, 5)
            mFirstNumber.setText(firstString)
        }

        mButton6.setOnClickListener {
            firstString = addNumberToString(firstString, 6)
            mFirstNumber.setText(firstString)
        }

        mButton7.setOnClickListener {
            firstString = addNumberToString(firstString, 7)
            mFirstNumber.setText(firstString)
        }

        mButton8.setOnClickListener {
            firstString = addNumberToString(firstString, 8)
            mFirstNumber.setText(firstString)
        }

        mButton9.setOnClickListener {
            firstString = addNumberToString(firstString, 9)
            mFirstNumber.setText(firstString)
        }

        mButtonAC.setOnClickListener {
            firstString = "0"
            previewString = ""
            isComma = false
            mFirstNumber.setText(firstString)
            mPreview.setText(previewString)
        }

        mButtonBksp.setOnClickListener {
            firstString = if (firstString.length > 1) {
                firstString.substring(0, firstString.length - 1)
            } else {
                "0"
            }
            if (firstString.length == 1 && firstString.get(0) == '-') {
                firstString = "0"
            }
            mFirstNumber.setText(firstString)
        }

        mButtonComma.setOnClickListener {
            isComma = firstString.contains(",")
            firstString += if (isComma) "" else {
                isComma = true
                ","
            }
            mFirstNumber.setText(firstString)
        }

        mButtonSign.setOnClickListener {
            if (!firstString.equals("0")) {
                firstString = if (firstString.get(0) == '-') {
                    firstString.substring(1, firstString.length)
                } else {
                    "-$firstString"
                }
            }
            mFirstNumber.setText(firstString)
        }

        mButtonPlus.setOnClickListener {
            if (!firstString.equals("0")) {
                if (previewString.isNotEmpty()) {
                    firstString =
                        equal(firstString, previewString, mFirstNumber, mPreview, operation)
                }
                previewString = firstString
                firstString = "0"
                operation = CalculatorSimple.MathOperation.ADDITION
                mFirstNumber.setText("0")
                mPreview.setText(previewString)
            }
        }

        mButtonSub.setOnClickListener {
            if (!firstString.equals("0")) {
                if (previewString.isNotEmpty()) {
                    firstString =
                        equal(firstString, previewString, mFirstNumber, mPreview, operation)
                }
                previewString = firstString
                firstString = "0"
                operation = CalculatorSimple.MathOperation.SUBTRACTION
                mFirstNumber.setText("0")
                mPreview.setText(previewString)
            }
        }

        mButtonMul.setOnClickListener {
            if (!firstString.equals("0")) {
                if (previewString.isNotEmpty()) {
                    firstString =
                        equal(firstString, previewString, mFirstNumber, mPreview, operation)
                }
                previewString = firstString
                firstString = "0"
                operation = CalculatorSimple.MathOperation.MULTIPLICATION
                mFirstNumber.setText("0")
                mPreview.setText(previewString)
            }

        }

        mButtonDiv.setOnClickListener {
            if (!firstString.equals("0")) {
                if (previewString.isNotEmpty()) {
                    firstString =
                        equal(firstString, previewString, mFirstNumber, mPreview, operation)
                }
                if (firstString.equals("err")) {
                    firstString = "0"
                    operation = CalculatorSimple.MathOperation.DIVISION
                } else {
                    previewString = firstString
                    firstString = "0"
                    operation = CalculatorSimple.MathOperation.DIVISION
                    mFirstNumber.setText("0")
                    mPreview.setText(previewString)
                }
            }
        }



        mButtonEqual.setOnClickListener {
            if (operation != CalculatorSimple.MathOperation.NONE) {
                firstString = equal(firstString, previewString, mFirstNumber, mPreview, operation)
                if (firstString.equals("err")) {
                    firstString = "0"
                    operation = CalculatorSimple.MathOperation.DIVISION
                } else {
                    operation = CalculatorSimple.MathOperation.NONE
                    previewString = ""
                }
                makeToast("Equal", firstString, previewString)
            }
        }
    }
}