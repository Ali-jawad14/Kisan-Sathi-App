"use client"

import type React from "react"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Card } from "@/components/ui/card"
import { Send, Bot } from "lucide-react"

interface Message {
  id: number
  text: string
  isUser: boolean
  timestamp: Date
}

export default function KisanSathiChat() {
  const [messages, setMessages] = useState<Message[]>([
    {
      id: 1,
      text: "السلام علیکم! میں کسان ساتھی چیٹ بوٹ ہوں۔ آپ کی زراعت سے متعلق کیسے مدد کر سکتا ہوں؟",
      isUser: false,
      timestamp: new Date(),
    },
  ])
  const [inputMessage, setInputMessage] = useState("")
  const [isTyping, setIsTyping] = useState(false)

  const suggestedQuestions = [
    "گندم کی بہترین اقسام کون سی ہیں؟",
    "چاول کی کاشت کا بہترین وقت کیا ہے؟",
    "کیڑوں سے فصلوں کو کیسے بچایا جائے؟",
    "سردی میں کون سی سبزیاں اگائی جا سکتی ہیں؟",
  ]

  const generateBotResponse = (userMessage: string): string => {
    if (userMessage.includes("گندم")) {
      return "گندم کی بہترین اقسام میں پنجاب-2011، فیصل آباد-2008، اور ملت-2011 شامل ہیں۔ یہ اقسام پاکستان کی آب و ہوا کے لیے موزوں ہیں اور زیادہ پیداوار دیتی ہیں۔"
    } else if (userMessage.includes("چاول")) {
      return "چاول کی کاشت کا بہترین وقت مئی سے جولائی تک ہے۔ خاص طور پر جون کا مہینہ سب سے موزوں ہے۔ بارش سے پہلے کاشت مکمل کر لیں۔"
    } else if (userMessage.includes("کیڑے") || userMessage.includes("کیڑوں")) {
      return "کیڑوں سے بچاؤ کے لیے: 1) صاف کھیتی کریں 2) مناسب کیڑے مار دوا استعمال کریں 3) فصل کی باری باری کریں 4) قدرتی دشمن کیڑوں کا استعمال کریں۔"
    } else if (userMessage.includes("سردی") || userMessage.includes("سبزیاں")) {
      return "سردی میں یہ سبزیاں اگا سکتے ہیں: گاجر، مولی، پالک، دھنیا، پیاز، لہسن، مٹر، اور گوبھی۔ یہ سب ٹھنڈے موسم میں اچھی پیداوار دیتی ہیں۔"
    } else if (userMessage.includes("آلو")) {
      return "آلو کی کاشت اکتوبر سے دسمبر تک کریں۔ اچھی نکاسی والی زمین استعمال کریں اور 15-20 سینٹی میٹر گہرائی میں بیج لگائیں۔"
    } else if (userMessage.includes("ٹماٹر")) {
      return "ٹماٹر کی کاشت کے لیے ستمبر سے نومبر کا وقت بہترین ہے۔ پودوں کے درمیان 45-60 سینٹی میٹر فاصلہ رکھیں۔"
    } else {
      return "آپ کے سوال کے لیے شکریہ۔ میں آپ کی مزید مدد کے لیے حاضر ہوں۔ برائے کرم اپنا سوال واضح کریں تاکہ میں بہتر جواب دے سکوں۔"
    }
  }

  const sendMessage = (messageText: string) => {
    if (!messageText.trim()) return

    // Add user message
    const userMessage: Message = {
      id: Date.now(),
      text: messageText,
      isUser: true,
      timestamp: new Date(),
    }

    setMessages((prev) => [...prev, userMessage])
    setInputMessage("")
    setIsTyping(true)

    // Simulate bot typing and response
    setTimeout(() => {
      const botResponse: Message = {
        id: Date.now() + 1,
        text: generateBotResponse(messageText),
        isUser: false,
        timestamp: new Date(),
      }
      setMessages((prev) => [...prev, botResponse])
      setIsTyping(false)
    }, 1500)
  }

  const handleSuggestedQuestion = (question: string) => {
    sendMessage(question)
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    sendMessage(inputMessage)
  }

  return (
    <div className="min-h-screen bg-white">
      {/* Header */}
      <div className="bg-[#1C7C54] text-white p-4 shadow-md">
        <div className="flex items-center justify-center gap-2">
          <Bot className="w-6 h-6" />
          <h1 className="text-xl font-bold">کسان ساتھی چیٹ بوٹ</h1>
        </div>
      </div>

      {/* Title */}
      <div className="text-center py-4 bg-gray-50">
        <h2 className="text-2xl font-bold text-black">کسان ساتھی چیٹ بوٹ</h2>
      </div>

      {/* Main Chat Container */}
      <div className="max-w-4xl mx-auto p-4 h-[calc(100vh-200px)] flex flex-col">
        {/* Chat Card */}
        <Card className="flex-1 flex flex-col mb-4 shadow-lg">
          {/* Messages Area */}
          <div className="flex-1 p-4 overflow-y-auto space-y-4 max-h-[400px]">
            {messages.map((message) => (
              <div key={message.id} className={`flex ${message.isUser ? "justify-end" : "justify-start"}`}>
                <div
                  className={`max-w-[280px] p-3 rounded-lg ${
                    message.isUser ? "bg-[#F1F1F1] text-black ml-auto" : "bg-[#C5E8D7] text-black mr-auto"
                  }`}
                  style={{ direction: "rtl", textAlign: "right" }}
                >
                  <p className="text-sm leading-relaxed">{message.text}</p>
                </div>
              </div>
            ))}

            {/* Typing Indicator */}
            {isTyping && (
              <div className="flex justify-start">
                <div className="bg-[#C5E8D7] text-black p-3 rounded-lg max-w-[280px]">
                  <div className="flex space-x-1">
                    <div className="w-2 h-2 bg-gray-500 rounded-full animate-bounce"></div>
                    <div
                      className="w-2 h-2 bg-gray-500 rounded-full animate-bounce"
                      style={{ animationDelay: "0.1s" }}
                    ></div>
                    <div
                      className="w-2 h-2 bg-gray-500 rounded-full animate-bounce"
                      style={{ animationDelay: "0.2s" }}
                    ></div>
                  </div>
                </div>
              </div>
            )}
          </div>

          {/* Divider */}
          <div className="h-px bg-[#EEEEEE]"></div>

          {/* Input Area */}
          <div className="p-3">
            <form onSubmit={handleSubmit} className="flex gap-2">
              <Button
                type="submit"
                size="icon"
                className="bg-[#1C7C54] hover:bg-[#155A3E] rounded-full w-10 h-10 flex-shrink-0"
              >
                <Send className="w-4 h-4" />
              </Button>
              <Input
                value={inputMessage}
                onChange={(e) => setInputMessage(e.target.value)}
                placeholder="اپنا سوال یہاں لکھیں..."
                className="flex-1 text-right border-[#CCCCCC] rounded-lg"
                style={{ direction: "rtl" }}
              />
            </form>
          </div>
        </Card>

        {/* Suggested Questions */}
        <div className="space-y-3">
          <h3 className="text-lg font-bold text-black text-right">مجوزہ سوالات:</h3>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
            {suggestedQuestions.map((question, index) => (
              <Button
                key={index}
                variant="outline"
                className="h-auto p-3 text-right border-[#1C7C54] text-black hover:bg-[#E5F5EE] whitespace-normal"
                style={{ direction: "rtl" }}
                onClick={() => handleSuggestedQuestion(question)}
              >
                {question}
              </Button>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}
