package com.example.kisansathi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kisansathi.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val messages = mutableListOf<Message>()
    private lateinit var adapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupClickListeners()
        addWelcomeMessage()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = MessageAdapter(messages)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun setupClickListeners() {
        // Send button
        binding.sendButton.setOnClickListener {
            val userMessage = binding.messageInput.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                sendMessage(userMessage)
            }
        }

        // Suggested questions
        binding.btnQuestion1.setOnClickListener {
            sendMessage("گندم کی بہترین اقسام کون سی ہیں؟")
        }
        
        binding.btnQuestion2.setOnClickListener {
            sendMessage("چاول کی کاشت کا بہترین وقت کیا ہے؟")
        }
        
        binding.btnQuestion3.setOnClickListener {
            sendMessage("کیڑوں سے فصلوں کو کیسے بچایا جائے؟")
        }
        
        binding.btnQuestion4.setOnClickListener {
            sendMessage("سردی میں کون سی سبزیاں اگائی جا سکتی ہیں؟")
        }
    }

    private fun addWelcomeMessage() {
        addMessage("السلام علیکم! میں کسان ساتھی چیٹ بوٹ ہوں۔ آپ کی زراعت سے متعلق کیسے مدد کر سکتا ہوں؟", isUser = false)
    }

    private fun sendMessage(userMessage: String) {
        addMessage(userMessage, isUser = true)
        binding.messageInput.text.clear()

        // Generate bot response
        generateBotResponse(userMessage)
    }

    private fun generateBotResponse(userMessage: String) {
        val response = when {
            userMessage.contains("گندم") -> {
                "گندم کی بہترین اقسام میں پنجاب-2011، فیصل آباد-2008، اور ملت-2011 شامل ہیں۔ یہ اقسام پاکستان کی آب و ہوا کے لیے موزوں ہیں اور زیادہ پیداوار دیتی ہیں۔ کاشت کا بہترین وقت اکتوبر سے نومبر تک ہے۔"
            }
            userMessage.contains("چاول") -> {
                "چاول کی کاشت کا بہترین وقت مئی سے جولائی تک ہے۔ خاص طور پر جون کا مہینہ سب سے موزوں ہے۔ بارش سے پہلے کاشت مکمل کر لیں۔ بہترین اقسام: بسمتی، سپر بسمتی، اور ایرری-6 ہیں۔"
            }
            userMessage.contains("کیڑے") || userMessage.contains("کیڑوں") -> {
                "کیڑوں سے بچاؤ کے لیے:\n1) کھیت کو صاف رکھیں\n2) مناسب کیڑے مار دوا استعمال کریں\n3) فصل کی باری باری کریں\n4) قدرتی دشمن کیڑوں کا استعمال کریں\n5) نیم کا تیل بھی مؤثر ہے"
            }
            userMessage.contains("سردی") || userMessage.contains("سبزیاں") -> {
                "سردی میں یہ سبزیاں اگا سکتے ہیں:\n• گاجر (اکتوبر-نومبر)\n• مولی (ستمبر-اکتوبر)\n• پالک (ستمبر-فروری)\n• دھنیا (اکتوبر-جنوری)\n• پیاز (اکتوبر-نومبر)\n• لہسن (اکتوبر-نومبر)\n• مٹر (اکتوبر-نومبر)\n• گوبھی (ستمبر-اکتوبر)"
            }
            userMessage.contains("آلو") || userMessage.contains("potato") -> {
                "آلو کی کاشت:\n• وقت: اکتوبر سے دسمبر تک\n• زمین: اچھی نکاسی والی\n• گہرائی: 15-20 سینٹی میٹر\n• فاصلہ: قطاروں میں 60 سینٹی میٹر\n• بہترین اقسام: کارڈنل، دیامنٹ، فیصل آباد وائٹ"
            }
            userMessage.contains("ٹماٹر") || userMessage.contains("tomato") -> {
                "ٹماٹر کی کاشت:\n• وقت: ستمبر سے نومبر\n• پودوں کا فاصلہ: 45-60 سینٹی میٹر\n• پانی: باقاعدگی سے لیکن زیادہ نہیں\n• بہترین اقسام: ریو گرانڈے، روما، نگینہ"
            }
            userMessage.contains("مرچ") || userMessage.contains("pepper") -> {
                "مرچ کی کاشت:\n• وقت: فروری-مارچ اور جولائی-اگست\n• درجہ حرارت: 20-30 ڈگری\n• پانی: متوازن مقدار\n• بہترین اقسام: ہری مرچ، لال مرچ، شملہ مرچ"
            }
            userMessage.contains("سیب") || userMessage.contains("apple") -> {
                "سیب کی کاشت:\n• موزوں علاقے: شمالی پاکستان (کشمیر، گلگت)\n• درجہ حرارت: ٹھنڈا موسم\n• مٹی: اچھی نکاسی والی\n• بہترین اقسام: ریڈ ڈیلیشس، گولڈن ڈیلیشس"
            }
            userMessage.contains("کھاد") || userMessage.contains("fertilizer") -> {
                "کھاد کا استعمال:\n• نائٹروجن: پتوں کی نشوونما کے لیے\n• فاسفورس: جڑوں کی مضبوطی\n• پوٹاشیم: پھلوں کی کوالٹی\n• گوبر کی کھاد: مٹی کی بہتری\n• وقت: کاشت سے پہلے اور دوران"
            }
            userMessage.contains("پانی") || userMessage.contains("آبپاشی") -> {
                "آبپاشی کے اصول:\n• صبح یا شام پانی دیں\n• زیادہ پانی نقصان دہ\n• ڈرپ سسٹم بہترین\n• مٹی کی نمی چیک کریں\n• موسم کے مطابق پانی دیں"
            }
            userMessage.contains("بیماری") || userMessage.contains("disease") -> {
                "فصلوں کی بیماریاں:\n• فنگل انفیکشن: نمی سے بچیں\n• وائرل: متاثرہ پودے ہٹائیں\n• بیکٹیریل: صاف آلات استعمال کریں\n• علاج: مناسب سپرے اور دوائیں"
            }
            else -> {
                "آپ کے سوال کے لیے شکریہ۔ میں آپ کی مزید مدد کے لیے حاضر ہوں۔ آپ مجھ سے یہ چیزوں کے بارے میں پوچھ سکتے ہیں:\n• فصلوں کی کاشت\n• بیماریوں کا علاج\n• کھاد کا استعمال\n• آبپاشی کے طریقے\n• کیڑوں سے بچاؤ"
            }
        }
        
        // Simulate typing delay
        binding.root.postDelayed({
            addMessage(response, isUser = false)
        }, 1500)
    }

    private fun addMessage(text: String, isUser: Boolean) {
        messages.add(Message(text, isUser))
        adapter.notifyItemInserted(messages.size - 1)
        binding.recyclerView.scrollToPosition(messages.size - 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
