package com.example.excrud

import org.junit.Assert
import org.junit.Test

class MemoUnitTest {

    @Test
    fun `메모의 날짜가 같고 고유 번호는 다를 때`(){
        val obj1 = Memo(5, "안녕하세요 반갑습니다", "2021-01-25 18:06:01", false)
        val obj2 = Memo(4, "안녕하세요 처음 뵙겠습니다", "2021-01-25 18:06:01", false)

        Assert.assertEquals(1 ,obj1.compareTo(obj2))
    }

}