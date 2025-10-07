import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Font
import java.awt.GridLayout
import javax.swing.*

class Calculator : JFrame("Калькулятор") {

    private val display = JTextField()
    private var currentInput = ""
    private var previousInput = ""
    private var operator = ""

    init {
        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setSize(300, 400)
        setLocationRelativeTo(null) // Центрирование окна

        // Настройка дисплея
        display.font = Font("Arial", Font.BOLD, 20)
        display.horizontalAlignment = JTextField.RIGHT
        display.isEditable = false
        add(display, BorderLayout.NORTH)

        // Создание панели с кнопками
        val buttonPanel = JPanel()
        buttonPanel.layout = GridLayout(5, 4, 5, 5)

        // Массив кнопок
        val buttons = arrayOf(
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "C", "CE", "⌫", "±"
        )

        // Создание кнопок
        buttons.forEach { text ->
            val button = JButton(text)
            button.font = Font("Arial", Font.BOLD, 16)
            button.addActionListener { handleButtonClick(text) }
            buttonPanel.add(button)
        }

        add(buttonPanel, BorderLayout.CENTER)
    }

    private fun setupListeners() {
        // Можно добавить обработку клавиатуры
    }

    private fun handleButtonClick(buttonText: String) {
        when {
            buttonText.matches(Regex("[0-9]")) -> handleNumber(buttonText)
            buttonText == "." -> handleDecimal()
            buttonText.matches(Regex("[+\\-*/]")) -> handleOperator(buttonText)
            buttonText == "=" -> calculateResult()
            buttonText == "C" -> clearAll()
            buttonText == "CE" -> clearEntry()
            buttonText == "⌫" -> handleBackspace()
            buttonText == "±" -> handleSignChange()
        }

        updateDisplay()
    }

    private fun handleNumber(number: String) {
        if (currentInput == "0") {
            currentInput = number
        } else {
            currentInput += number
        }
    }

    private fun handleDecimal() {
        if (!currentInput.contains(".")) {
            if (currentInput.isEmpty()) {
                currentInput = "0."
            } else {
                currentInput += "."
            }
        }
    }

    private fun handleOperator(newOperator: String) {
        if (currentInput.isNotEmpty()) {
            if (previousInput.isNotEmpty() && operator.isNotEmpty() && currentInput.isNotEmpty()) {
                calculateResult()
            }
            operator = newOperator
            previousInput = currentInput
            currentInput = ""
        }
    }

    private fun calculateResult() {
        if (previousInput.isNotEmpty() && currentInput.isNotEmpty() && operator.isNotEmpty()) {
            try {
                val num1 = previousInput.toDouble()
                val num2 = currentInput.toDouble()
                val result = when (operator) {
                    "+" -> num1 + num2
                    "-" -> num1 - num2
                    "*" -> num1 * num2
                    "/" -> if (num2 != 0.0) num1 / num2 else throw ArithmeticException("Деление на ноль")
                    else -> 0.0
                }

                currentInput = if (result % 1 == 0.0) {
                    result.toInt().toString()
                } else {
                    String.format("%.6f", result).trimEnd('0').trimEnd('.')
                }
                operator = ""
                previousInput = ""
            } catch (e: ArithmeticException) {
                currentInput = "Ошибка"
                operator = ""
                previousInput = ""
            }
        }
    }

    private fun clearAll() {
        currentInput = ""
        previousInput = ""
        operator = ""
    }

    private fun clearEntry() {
        currentInput = ""
    }

    private fun handleBackspace() {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
        }
    }

    private fun handleSignChange() {
        if (currentInput.isNotEmpty() && currentInput != "0") {
            currentInput = if (currentInput.startsWith("-")) {
                currentInput.substring(1)
            } else {
                "-$currentInput"
            }
        }
    }

    private fun updateDisplay() {
        display.text = if (currentInput.isEmpty()) "0" else currentInput
    }
}

fun main() {
    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    } catch (e: Exception) {
        e.printStackTrace()
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName())
        } catch (e2: Exception) {
            e2.printStackTrace()
        }
    }

    SwingUtilities.invokeLater {
        val calculator = Calculator()
        calculator.isVisible = true
    }
}