package com.gmail.pavlovsv93.verification

const val COLLECTION = "verifications"


/*
	-->> Установлен -> Снят в поверку -> Поверяется ----->

  	-----> Установлен -->>
  	-----> Забракован -->

  					  -->Ремонтируеться 	-> Установлен -->>
  										    -> Утилизирован
  					  -->Утилизирован
*/
val statusListWaiting by lazy {
	arrayOf(
		STATUS_INSTALLED,
		STATUS_SAVED
	)
}
val statusListWaitingReturn by lazy {
	arrayOf(
		STATUS_FIT,
		STATUS_REJECTED
	)
}
val statusListVerification by lazy {
	arrayOf(
		STATUS_REMOVED
	)
}
const val CHECKED_ITEM = 1
const val STATUS_INSTALLED = "Установлен"
const val STATUS_REMOVED = "Снят в поверку"
const val STATUS_VERIFICATION = "Поверяется"
const val STATUS_TRUSTED = "Поверен(Получен)"
const val STATUS_REJECTED = "Забракован"
const val STATUS_FIT = "Годен"
const val STATUS_FIX = "Ремонтируется"
const val STATUS_WAIT_FIX = "Ожидает ремонт"
const val STATUS_REMOTE = "Утилизирован"
const val STATUS_NEW = "НОВЫЙ"
const val STATUS_SAVED = "Хранение"
const val STATUS_SAVE = "Длительное хранение"
const val STATUS_ERROR = "Ошибка статуса"

val statusList = listOf(
	STATUS_NEW,
	STATUS_INSTALLED,
	STATUS_REMOVED,
	STATUS_VERIFICATION,
	STATUS_TRUSTED,
	STATUS_REJECTED,
	STATUS_FIX,
	STATUS_REMOTE,
	STATUS_WAIT_FIX,
	STATUS_SAVED,
	STATUS_SAVE
)


const val MODEL_SSS_07_08 = "ССС-903сл"
const val MODEL_SSS_08_14 = "ССС-903"
const val MODEL_SSS_ST = "ССС-903st"
const val MODEL_SSS_Mst = "ССС-903Mst"
const val MODEL_SSS_M = "ССС-903M"
const val MODEL_SSS_MT = "ССС-903MT"
const val MODEL_GANK = "ГАНК-4С"
const val MODEL_DAK = "ДАК"
const val MODEL_DAH_M = "ДАХ-М"

const val MODEL_TESTO_6621 = "Testo 6621"
const val MODEL_TESTO_6651 = "Testo 6651"
const val MODEL_TSPU = "ТСПУ-205"
const val MODEL_IPTV = "ИПТВ-206"
const val MODEL_PVT = "ПВТ-100"
const val MODEL_DGS = "ДГС-125"
const val MODEL_TTM = "ТТМ-2-04"

const val MODEL_TESTO_VELOSITY = "Testo Velocity"
const val MODEL_SRPS = "СРПС-05Д"
const val MODEL_BDKG = "БДКГ-204"
const val MODEL_AEROKON = "АЭРОКОН-М"

const val KEY_ENTITY_PARAMETER = "KEY_ENTITY_PARAMETER"
const val ARG_UID = "ARG_UID"
const val ARG_NUMBER = "ARG_NUMBER"
const val ARG_DATE = "ARG_DATE"
const val ARG_MODEL = "ARG_MODEL"
const val ARG_PARAMETER = "ARG_PARAMETER"
const val ARG_STATUS = "ARG_STATUS"
const val ARG_LOCATION = "ARG_LOCATION"

const val KEY_NUMBER = "number"
const val KEY_DATE = "date"
const val KEY_MODEL = "model"
const val KEY_PARAMETER = "parameter"
const val KEY_STATION_KEY = "keyStation"
const val KEY_STATION = "station"
const val KEY_POSITION = "position"
const val KEY_DESCRIPTION = "description"
const val KEY_NEXT_DATE = "nextDate"
const val KEY_STATUS = "status"
const val KEY_INFO = "info"
const val KEY_VERIFICATION = "verification"

const val IS_SUCCESS = "Успешно!"
const val IS_EXCEPTION = "Не известная ошибка!"

val keyStation = listOf(
	110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124,
	209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223,
	313, 314, 315, 316, 800, 900
)
val station = listOf(
	"Малиновка",
	"Петровщина",
	"Михалова",
	"Грушевка",
	"Институт культуры",
	"Пл.Ленина",
	"Октябрьская",
	"Пл.Победы",
	"Пл.Я.Коласа",
	"Академия наук",
	"Парк Челюскинцев",
	"Московская",
	"Восток",
	"Борисовский тракт",
	"Уручье",
	"Могилевская",
	"Автозаводская",
	"Партизанская",
	"Тракторный завод",
	"Пролетарская",
	"Первомайская",
	"Купаловская",
	"Немига",
	"Фрунзенская",
	"Молодежная",
	"Пушкинская",
	"Спортивная",
	"Кунцевщина",
	"Каменная горка",
	"Ковальская Слобода",
	"Вокзальная",
	"Пл.Ф.Богушевича",
	"Юбилейная Пл.",
	"ЗЭП",
	"Нет позиции"
)
val verificationList = listOf(1, 2, 3, 4, 5, 6)
val parameterList = listOf(
	"CO - Оксид углерода",
	"CO2 - Диоксид углерода",
	"CH4 - Метан",
	"Cl2 - Хлор",
	"NH3 - Аммиак",
	"H2SO4 - Серная кислота",
	"HCl - Соляная кислота",
	"Пыль",
	"Температура",
	"Влажность",
	"Темп./Влажн.",
	"Подвижность воздуха",
	"Радиационный фон"
)
val modelList = listOf(
	MODEL_SSS_07_08,
	MODEL_SSS_08_14,
	MODEL_SSS_ST,
	MODEL_SSS_Mst,
	MODEL_SSS_M,
	MODEL_SSS_MT,
	MODEL_GANK,
	MODEL_DAK,
	MODEL_DAH_M,
	MODEL_TESTO_6621,
	MODEL_TESTO_6651,
	MODEL_TSPU,
	MODEL_IPTV,
	MODEL_PVT,
	MODEL_DGS,
	MODEL_TESTO_VELOSITY,
	MODEL_SRPS,
	MODEL_BDKG,
	MODEL_AEROKON,
	MODEL_TTM
)

