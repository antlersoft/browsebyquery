ifeq ($(cygwin),)
	LIB_UTIL = -lutil
else
	LIB_UTIL =
endif
ifeq ($(release),)
	CXXFLAGS = -g
	LDFLAGS = -g $(LIB_UTIL)
	DEBUG_TAG = D
else
	CXXFLAGS = -O
	LDFLAGS = $(LIB_UTIL)
	DEBUG_TAG = 
endif

DEPENDS = depend.mk

UTIL_LIB = $(CPP_HOME)/com/antlersoft/com_antlersoft_lib$(DEBUG_TAG).a

SRCS = javagen.cpp

OBJS = javagen.o

EXES = javagen

CPPFLAGS = -I $(CPP_HOME)

.cpp.y :
	cpp $(CPPFLAGS) $^ $@

all :  $(EXES)

deps : $(DEPENDS)

clean :
	rm -f $(DEPENDS) $(EXES) $(OBJS)

$(EXES) : $(OBJS) $(UTIL_LIB) $(CPP_HOME)/com/antlersoft/parser/libparser.a
	g++ -o $@ $(LDFLAGS) $+

$(DEPENDS) :
	touch $@
	makedepend -f $(DEPENDS) -- -I /usr/include/g++-3 $(CPPFLAGS) -- $(SRCS)

include $(DEPENDS)
