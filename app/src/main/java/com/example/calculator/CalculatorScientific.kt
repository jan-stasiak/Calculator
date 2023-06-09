package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.*

class CalculatorScientific : AppCompatActivity() {
    enum class MathOperation {
        ADDITION,
        SUBTRACTION,
        MULTIPLICATION,
        DIVISION,
        POW,
        NONE
    }

    enum class MathFunction {
        SIN,
        COS,
        TAN,
        LN,
        SQRT,
        LOG,
        SQUARE
    }

    private fun makeToast(vararg texts: String) {
        Toast.makeText(
            this@CalculatorScientific,
            texts.joinToString(separator = "\n"),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun addNumberToString(inputString: String, number: Number): String {
        val resultString = if (inputString != "0") {
            inputString + number.toString()
        } else {
            number.toString()
        }
        return resultString
    }

    private fun equal(
        firstString: String,
        previewString: String,
        mFirstNumber: TextView,
        mPreview: TextView,
        operation: MathOperation
    ): String {
        if (previewString.isEmpty()) {
            return ""
        }

        var firstNum = previewString.replace(',', '.').toBigDecimal()
        var secondNum = firstString.replace(',', '.').toBigDecimal()

        val regex = Regex("[0].[0]*")
        if (regex.matches(secondNum.toString())) {
            secondNum = BigDecimal(0)
        }
        if (regex.matches(firstNum.toString())) {
            firstNum = BigDecimal(0)
        }

        val s =
            if (operation == MathOperation.DIVISION && secondNum == BigDecimal.ZERO) {
                return "err"
            } else {
                val sum = when (operation) {
                    MathOperation.MULTIPLICATION -> firstNum * secondNum
                    MathOperation.ADDITION -> firstNum + secondNum
                    MathOperation.SUBTRACTION -> firstNum - secondNum
                    MathOperation.DIVISION -> firstNum.divide(
                        secondNum,
                        8,
                        RoundingMode.HALF_UP
                    )
                        .stripTrailingZeros()
                    MathOperation.POW -> {
                        val result = firstNum.toDouble().pow(secondNum.toDouble())
                        if (result.isInfinite()) {
                            makeToast("Błąd obliczeń: wynik dąży do nieskończoności")
                            BigDecimal(0)
                        } else {
                            result.toBigDecimal()
                        }
                    }
                    MathOperation.NONE -> return ""
                }
                val df = DecimalFormat("#.########")
                df.format(sum.stripTrailingZeros()).toString().replace('.', ',')
            }

        if  (s == "err") {
            makeToast("Nie dziel przez 0!")
            return s;
        }
        if (isTextFit(s, mFirstNumber)) {
            mFirstNumber.text = s
            mPreview.text = ""
        } else {
            makeToast("Wynik przekroczył maksymalną liczbę znaków!", "Otrzymany wynik to: " + s)
            mFirstNumber.text = 0.toString()
            mPreview.text = ""
            return 0.toString()
        }

        return s
    }

    private fun functionsCalc(function: MathFunction, firstString: String): String {
        this.operation = MathOperation.NONE
        if (firstString.isNotEmpty()) {
            var value = firstString.replace(',', '.').toBigDecimal()
            when (function) {
                MathFunction.SIN -> value = sin(value.toDouble()).toBigDecimal()
                MathFunction.COS -> value = cos(value.toDouble()).toBigDecimal()
                MathFunction.TAN -> value = tan(value.toDouble()).toBigDecimal()
                MathFunction.LN -> {
                    value = if (value > BigDecimal(0)) {
                        ln(value.toDouble()).toBigDecimal()
                    } else {
                        makeToast("Niepoprawne dane")
                        BigDecimal(0)
                    }
                }
                MathFunction.SQRT -> {
                    value = if (value >= BigDecimal(0)) {
                        sqrt(value.toDouble()).toBigDecimal()
                    } else {
                        makeToast("Niepoprawne dane")
                        BigDecimal(0)
                    }
                }
                MathFunction.LOG -> {
                    value = if (value > BigDecimal(0)) {
                        log10(value.toDouble()).toBigDecimal()
                    } else {
                        makeToast("Niepoprawne dane")
                        BigDecimal(0)
                    }
                }
                MathFunction.SQUARE -> {
                    val result = value.toDouble().pow(2)
                    if (result.isInfinite()) {
                        makeToast("Wynik wyszedł poza zakres")
                        value = BigDecimal(0)
                    } else {
                        value = result.toBigDecimal()
                    }
                }
            }
            val df = DecimalFormat("#.########")
            return df.format(value.stripTrailingZeros()).toString().replace('.', ',')
        }
        return "0"
    }

    private fun writeOperation() {
        when (operation) {
            MathOperation.ADDITION -> mSign.text = "+"
            MathOperation.SUBTRACTION -> mSign.text = "-"
            MathOperation.MULTIPLICATION -> mSign.text = "*"
            MathOperation.DIVISION -> mSign.text = "/"
            MathOperation.POW -> mSign.text = "^"
            MathOperation.NONE -> mSign.text = ""
        }

    }

    private fun isTextFit(text: String, textView: TextView) =
        textView.paint.measureText(text) <= (textView.width - textView.paddingLeft - textView.paddingRight)

    private lateinit var mFirstNumber: TextView
    private lateinit var mPreview: TextView
    lateinit var mSign: TextView
    private var firstString: String = "0"
    private var previewString: String = ""
    private var operation: MathOperation = MathOperation.NONE
    private var isComma: Boolean = false

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator_scientific)

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
        val mButtonBksp = findViewById<Button>(R.id.buttonBksp)
        val mButtonC = findViewById<Button>(R.id.buttonC)
        val mButtonComma = findViewById<Button>(R.id.buttonComma)
        val mButtonSign = findViewById<Button>(R.id.buttonSign)
        val mButtonPlus = findViewById<Button>(R.id.buttonPlus)
        val mButtonEqual = findViewById<Button>(R.id.buttonEqual)
        val mButtonSub = findViewById<Button>(R.id.buttonSub)
        val mButtonMul = findViewById<Button>(R.id.buttonMul)
        val mButtonDiv = findViewById<Button>(R.id.buttonDiv)
        val mButtonSin = findViewById<Button>(R.id.buttonSin)
        val mButtonCos = findViewById<Button>(R.id.buttonCos)
        val mButtonTan = findViewById<Button>(R.id.buttonTan)
        val mButtonLn = findViewById<Button>(R.id.buttonLn)
        val mButtonSqrt = findViewById<Button>(R.id.buttonSqrt)
        val mButtonLog = findViewById<Button>(R.id.buttonLog)
        val mButtonPow = findViewById<Button>(R.id.buttonXY)
        val mButtonPowSqrt = findViewById<Button>(R.id.buttonXSquare)

        mButton0.setOnClickListener {
            if (firstString != "0") firstString += "0"
            mFirstNumber.text = firstString
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
            mFirstNumber.text = firstString
            mPreview.text = previewString
            operation = MathOperation.NONE
            writeOperation()
        }

        mButtonBksp.setOnClickListener {
            firstString = if (firstString.length > 1) {
                firstString.substring(0, firstString.length - 1)
            } else {
                "0"
            }
            if (firstString.length == 1 && firstString[0] == '-') {
                firstString = "0"
            }
            mFirstNumber.text = firstString
        }

        mButtonComma.setOnClickListener {
            isComma = firstString.contains(",")
            firstString += if (isComma) "" else {
                isComma = true
                ","
            }
            mFirstNumber.text = firstString
        }

        mButtonSign.setOnClickListener {
            if (!firstString.equals("0")) {
                firstString = if (firstString.get(0) == '-') {
                    firstString.substring(1, firstString.length)
                } else {
                    "-$firstString"
                }
            }
            mFirstNumber.text = firstString
        }

        mButtonPlus.setOnClickListener {
            if (previewString.isNotEmpty()) {
                operation = MathOperation.ADDITION
                firstString =
                    equal(firstString, previewString, mFirstNumber, mPreview, operation)
            }
            if (isTextFit(firstString, mFirstNumber)) {
                previewString = firstString
                firstString = "0"
                operation = MathOperation.ADDITION
                mFirstNumber.text = "0"
                mPreview.text = previewString
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
                operation = MathOperation.SUBTRACTION
                firstString =
                    equal(firstString, previewString, mFirstNumber, mPreview, operation)
            }
            if (isTextFit(firstString, mFirstNumber)) {
                previewString = firstString
                firstString = "0"
                operation = MathOperation.SUBTRACTION
                mFirstNumber.text = "0"
                mPreview.text = previewString
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
                operation = MathOperation.MULTIPLICATION
                firstString =
                    equal(firstString, previewString, mFirstNumber, mPreview, operation)
            }
            if (isTextFit(firstString, mFirstNumber)) {
                previewString = firstString
                firstString = "0"
                operation = MathOperation.MULTIPLICATION
                mFirstNumber.text = "0"
                mPreview.text = previewString
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
                operation = MathOperation.DIVISION
                firstString =
                    equal(firstString, previewString, mFirstNumber, mPreview, operation)
            }
            if (firstString.equals("err")) {
                firstString = "0"
                mFirstNumber.text = firstString
                operation = MathOperation.DIVISION
            } else {
                previewString = firstString
                firstString = "0"
                operation = MathOperation.DIVISION
                mFirstNumber.text = "0"
                mPreview.text = previewString
            }
            writeOperation()
        }



        mButtonEqual.setOnClickListener {
            if (operation != MathOperation.NONE) {
                firstString = equal(firstString, previewString, mFirstNumber, mPreview, operation)
                if (firstString.equals("err")) {
                    makeToast("Nie dziel przez zero!")
                    firstString = "0"
                    operation = MathOperation.DIVISION
                    mFirstNumber.text = "0"
                } else {
                    operation = MathOperation.NONE
                    previewString = ""
                }
            }
            writeOperation()
        }

        mButtonSin.setOnClickListener {
            firstString = functionsCalc(MathFunction.SIN, firstString)
            mFirstNumber.text = firstString
            previewString = ""
            mPreview.text = previewString
        }

        mButtonCos.setOnClickListener {
            firstString = functionsCalc(MathFunction.COS, firstString)
            mFirstNumber.text = firstString
            previewString = ""
            mPreview.text = previewString
        }

        mButtonTan.setOnClickListener {
            firstString = functionsCalc(MathFunction.TAN, firstString)
            mFirstNumber.text = firstString
            previewString = ""
            mPreview.text = previewString
        }

        mButtonLn.setOnClickListener {
            firstString = functionsCalc(MathFunction.LN, firstString)
            mFirstNumber.text = firstString
            previewString = ""
            mPreview.text = previewString
        }

        mButtonSqrt.setOnClickListener {
            firstString = functionsCalc(MathFunction.SQRT, firstString)
            mFirstNumber.text = firstString
            previewString = ""
            mPreview.text = previewString
        }

        mButtonPowSqrt.setOnClickListener {
            firstString = functionsCalc(MathFunction.SQUARE, firstString)
            if (isTextFit(firstString, mFirstNumber)) {
                mFirstNumber.text = firstString
                previewString = ""
                mPreview.text = previewString
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
        }

        mButtonLog.setOnClickListener {
            firstString = functionsCalc(MathFunction.LOG, firstString)
            mFirstNumber.text = firstString
            previewString = ""
            mPreview.text = previewString
        }

        mButtonPow.setOnClickListener {
            if (!firstString.equals("0")) {

                if (previewString.isNotEmpty()) {
                    operation = MathOperation.POW
                    firstString =
                        equal(firstString, previewString, mFirstNumber, mPreview, operation)
                }
                if (isTextFit(firstString, mFirstNumber)) {
                    previewString = firstString
                    firstString = "0"
                    operation = MathOperation.POW
                    mFirstNumber.text = "0"
                    mPreview.text = previewString
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
            }
            writeOperation()
        }
    }
}