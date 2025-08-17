package structs_test

import (
	"from-java-to-go/internal/structs"
	"from-java-to-go/internal/structs/mocks"
	"testing"

	"go.uber.org/mock/gomock"
)

func TestEngine_String(t *testing.T) {
	tests := []struct {
		power int
		want  string
	}{
		{123, "Engine with power 123"},
		{0, "Engine with power 0"},
		{-10, "Engine with power -10"},
		{9999, "Engine with power 9999"},
	}

	for _, tt := range tests {
		engine := structs.Engine{Power: tt.power}
		got := engine.String()
		if got != tt.want {
			t.Errorf("Engine{Power: %d}.String() = %q, want %q", tt.power, got, tt.want)
		}
	}
}

func TestEngine_Mock(t *testing.T) {
	ctrl := gomock.NewController(t)
	defer ctrl.Finish()

	mockStarter := mocks.NewMockStarter(ctrl)

	// Устанавливаем ожидание на метод Start
	mockStarter.EXPECT().Start().Times(1)

	// Вызываем метод Start
	mockStarter.Start()
}
