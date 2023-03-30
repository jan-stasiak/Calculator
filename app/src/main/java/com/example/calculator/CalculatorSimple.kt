package com.example.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

class CalculatorSimple : AppCompatActivity() {
    enum class MathOperation {
        ADDITION,
        SUBTRACTION,
        MULTIPLICATION,
        DIVISION,
        NONE
    }

    fun makeToast(vararg texts: String) {
        Toast.makeText(
            this@CalculatorSimple,
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
        operation: MathOperation
    ): String {
        if (previewString.isEmpty()) {
            return ""
        }

        val firstNum = previewString.replace(',', '.').toBigDecimal()
        val secondNum = firstString.replace(',', '.').toBigDecimal()

        val s = if (operation == MathOperation.DIVISION && secondNum == BigDecimal.ZERO) {
            makeToast("Nie dziel przez 0!")
            return "err"
        } else {
            val sum = when (operation) {
                MathOperation.MULTIPLICATION -> firstNum * secondNum
                MathOperation.ADDITION -> firstNum + secondNum
                MathOperation.SUBTRACTION -> firstNum - secondNum
                MathOperation.DIVISION -> firstNum.divide(secondNum, 8, RoundingMode.HALF_UP)
                    .stripTrailingZeros()
                MathOperation.NONE -> return ""
            }
            val df = DecimalFormat("#.########")
            df.format(sum.stripTrailingZeros()).toString().replace('.', ',')
        }

        writeOperation()
        mFirstNumber.text = s
        mPreview.text = ""
        return s
    }

    private fun writeOperation() {
        when (operation) {
            MathOperation.ADDITION -> mSign.text = "+"
            MathOperation.SUBTRACTION -> mSign.text = "-"
            MathOperation.MULTIPLICATION -> mSign.text = "*"
            MathOperation.DIVISION -> mSign.text = "/"
            MathOperation.NONE -> mSign.text = ""
        }

    }

    lateinit var mFirstNumber: TextView
    lateinit var mPreview: TextView
    lateinit var mSign: TextView
    var firstString: String = "0"
    var previewString: String = ""
    var operation: MathOperation = MathOperation.NONE
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
        operation = MathOperation.valueOf(savedInstanceState.getString("operation")!!)

        mFirstNumber = findViewById(R.id.mainNumber) ?: mFirstNumber
        mPreview = findViewById(R.id.preview) ?: mPreview
        mSign = findViewById(R.id.textViewSign) ?: mSign

        writeOperation()
        mFirstNumber.text = firstString
        mPreview.text = previewString
    }

    private fun isTextFit(text: String, textView: TextView) =
        textView.paint.measureText(text) <= (textView.width - textView.paddingLeft - textView.paddingRight)

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator_simple)

        val mFirstNumber = findViewById<TextView>(R.id.mainNumber)
        val mPreview = findViewById<TextView>(R.id.preview)
        mFirstNumber.text = firstString
        mPreview.text = previewString

        mSign = findViewById(R.id.textViewSign)
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
        val mButtonBksp = findViewById<Button>(R.id.bksp)
        val mButtonC = findViewById<Button>(R.id.AC)
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
            if (isTextFit(firstString + 1, mFirstNumber)) {
                firstString = addNumberToString(firstString, 1)
                mFirstNumber.text = firstString
            } else {
                makeToast("Osiągnięto maksymalną ilość znaków!")
            }
        }

        mButton2.setOnClickListener {
            if (isTextFit(firstString + 2, mFirstNumber)) {
                firstString = addNumberToString(firstString, 2)
                mFirstNumber.text = firstString
            } else {
                makeToast("Osiągnięto maksymalną ilość znaków!")
            }
        }

        mButton3.setOnClickListener {
            if (isTextFit(firstString + 3, mFirstNumber)) {
                firstString = addNumberToString(firstString, 3)
                mFirstNumber.text = firstString
            } else {
                makeToast("Osiągnięto maksymalną ilość znaków!")
            }
        }

        mButton4.setOnClickListener {
            if (isTextFit(firstString + 4, mFirstNumber)) {
                firstString = addNumberToString(firstString, 4)
                mFirstNumber.text = firstString
            } else {
                makeToast("Osiągnięto maksymalną ilość znaków!")
            }
        }

        mButton5.setOnClickListener {
            if (isTextFit(firstString + 5, mFirstNumber)) {
                firstString = addNumberToString(firstString, 5)
                mFirstNumber.text = firstString
            } else {
                makeToast("Osiągnięto maksymalną ilość znaków!")
            }
        }

        mButton6.setOnClickListener {
            if (isTextFit(firstString + 6, mFirstNumber)) {
                firstString = addNumberToString(firstString, 6)
                mFirstNumber.text = firstString
            } else {
                makeToast("Osiągnięto maksymalną ilość znaków!")
            }
        }

        mButton7.setOnClickListener {
            if (isTextFit(firstString + 7, mFirstNumber)) {
                firstString = addNumberToString(firstString, 7)
                mFirstNumber.text = firstString
            } else {
                makeToast("Osiągnięto maksymalną ilość znaków!")
            }
        }

        mButton8.setOnClickListener {
            if (isTextFit(firstString + 8, mFirstNumber)) {
                firstString = addNumberToString(firstString, 8)
                mFirstNumber.text = firstString
            } else {
                makeToast("Osiągnięto maksymalną ilość znaków!")
            }
        }

        mButton9.setOnClickListener {
            if (isTextFit(firstString + 9, mFirstNumber)) {
                firstString = addNumberToString(firstString, 9)
                mFirstNumber.text = firstString
            } else {
                makeToast("Osiągnięto maksymalną ilość znaków!")
            }
        }

        mButtonC.setOnClickListener {
            firstString = "0"
            previewString = ""
            isComma = false
            mFirstNumber.setText(firstString)
            mPreview.setText(previewString)
            operation = MathOperation.NONE
            writeOperation()
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
            if (previewString.isNotEmpty()) {
                firstString =
                    equal(firstString, previewString, mFirstNumber, mPreview, operation)
            }
            if (isTextFit(firstString, mFirstNumber)) {
                previewString = firstString
                firstString = "0"
                operation = MathOperation.ADDITION
                mFirstNumber.setText("0")
                mPreview.setText(previewString)
            } else {
                makeToast(
                    "Wynik przekroczył maksymalną liczbę znaków",
                    "Otrzymany wynik to: " + firstString
                )
                firstString = "0"
                previewString = ""
                mFirstNumber.text = firstString
                mPreview.text = previewString
            }
            writeOperation()
        }

        mButtonSub.setOnClickListener {
            if (previewString.isNotEmpty()) {
                firstString =
                    equal(firstString, previewString, mFirstNumber, mPreview, operation)
            }
            if (isTextFit(firstString, mFirstNumber)) {
                previewString = firstString
                firstString = "0"
                operation = MathOperation.SUBTRACTION
                mFirstNumber.setText("0")
                mPreview.setText(previewString)
            } else {
                makeToast(
                    "Wynik przekroczył maksymalną liczbę znaków",
                    "Otrzymany wynik to: " + firstString
                )
                firstString = "0"
                previewString = ""
                mFirstNumber.text = firstString
                mPreview.text = previewString
            }
            writeOperation()
        }

        mButtonMul.setOnClickListener {
            if (previewString.isNotEmpty()) {
                firstString =
                    equal(firstString, previewString, mFirstNumber, mPreview, operation)
            }
            if (isTextFit(firstString, mFirstNumber)) {
                previewString = firstString
                firstString = "0"
                operation = MathOperation.MULTIPLICATION
                mFirstNumber.setText("0")
                mPreview.setText(previewString)
            } else {
                makeToast(
                    "Wynik przekroczył maksymalną liczbę znaków",
                    "Otrzymany wynik to: " + firstString
                )
                firstString = "0"
                previewString = ""
                mFirstNumber.text = firstString
                mPreview.text = previewString
            }

            writeOperation()
        }

        mButtonDiv.setOnClickListener {
            if (previewString.isNotEmpty()) {
                firstString =
                    equal(firstString, previewString, mFirstNumber, mPreview, operation)
            }
            if (firstString.equals("err")) {
                firstString = "0"
                operation = MathOperation.DIVISION
            } else {
                previewString = firstString
                firstString = "0"
                operation = MathOperation.DIVISION
                mFirstNumber.setText("0")
                mPreview.setText(previewString)
            }
            writeOperation()
        }



        mButtonEqual.setOnClickListener {
            if (operation != MathOperation.NONE) {
                firstString = equal(firstString, previewString, mFirstNumber, mPreview, operation)
                if (firstString.equals("err")) {
                    firstString = "0"
                    operation = MathOperation.DIVISION
                } else {
                    operation = MathOperation.NONE
                    previewString = ""
                }
            }
            writeOperation()
        }
    }
}