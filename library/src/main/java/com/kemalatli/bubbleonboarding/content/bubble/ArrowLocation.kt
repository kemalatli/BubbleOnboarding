package com.kemalatli.bubbleonboarding.content.bubble

sealed class ArrowLocation {

    class Left(): ArrowLocation()
    class Right(): ArrowLocation()
    class Top(): ArrowLocation()
    class Bottom(): ArrowLocation()

}